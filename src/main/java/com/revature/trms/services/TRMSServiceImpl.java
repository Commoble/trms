package com.revature.trms.services;

import com.revature.trms.models.Approval;
import com.revature.trms.models.Department;
import com.revature.trms.models.Employee;
import com.revature.trms.models.EventType;
import com.revature.trms.models.GradingFormat;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.models.Role;
import com.revature.trms.repositories.TRMSDao;
import com.revature.trms.requestmodels.NewApprovalRequestJson;
import com.revature.trms.requestmodels.NewRequestRequestJson;
import com.revature.trms.responsemodels.ApprovalResponse;
import com.revature.trms.responsemodels.EmployeeResponse;
import com.revature.trms.responsemodels.NewRequestFormResponse;
import com.revature.trms.responsemodels.RequestResponse;
import com.revature.trms.util.ApprovalLevel;
import com.revature.trms.util.Constants;
import com.revature.trms.util.ParseUtils;
import com.revature.trms.util.RequestStatus;
import com.revature.trms.util.ServiceResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TRMSServiceImpl implements TRMSService
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final TRMSDao repository;
	private final String hostURL;

	public TRMSServiceImpl(TRMSDao repository, String hostURL)
	{
		this.repository = repository;
		this.hostURL = hostURL;
	}

	@Nullable
	@Override
	public Employee getEmployeeByLogin(String username, String password)
	{
		return this.repository.getEmployee(username, password);
	}

	@Nullable
	@Override
	public EmployeeResponse getEmployeeResponse(int loginID, int employeeID)
	{
		LOGGER.debug("Getting employee {}", employeeID);
		if (loginID == employeeID)
		{
			@Nullable Employee employee = this.repository.getEmployeeDao().get(employeeID);
			if (employee == null)
			{
				LOGGER.debug("Could not get employee {}: Requested ID not found", employeeID);
				return null;
			}
			EmployeeResponse response = this.getEmployeeResponse(employee);
			LOGGER.debug("Retrieved employee {}", employeeID);
			return response;
		}
		else
		{
			LOGGER.debug("Could not get employee {}: Requesting user not authorized", employeeID);
			return null;
		}
	}

	@Override
	public int getRemainingReimbursement(int employeeID)
	{
		LOGGER.debug("Getting remaining reimbursement amount for employee {}", employeeID);

		List<ReimbursementRequest> requests = this.getAwardedOrPendingRequests(employeeID);
		LocalDate currentDay = LocalDate.now(Clock.systemUTC());
		int maximumPerYear = Constants.YEARLY_MAX_REIMBURSEMENT; // in american cents
		// amount available resets on the new year
		LocalDateTime startOfYear = LocalDate.ofYearDay(currentDay.getYear(), 1).atStartOfDay();
		long startOfYearMillis = startOfYear.toEpochSecond(ZoneOffset.UTC) * 1000;
		int spentReimbursement = requests.stream()
			.filter(request -> request.getSubmissionTime() >= startOfYearMillis)
			.map(ReimbursementRequest::getReimbursementAmount)
			.reduce(0, (a,b)->a+b);
		return Math.max(0, maximumPerYear - spentReimbursement);
	}

	@Nullable
	@Override
	public ServiceResult<RequestResponse> addRequest(int loginID, NewRequestRequestJson json)
	{
		LOGGER.debug("Adding reimbursement request");

		ServiceResult<RequestResponse> notFound = ServiceResult.empty(HttpStatus.NOT_FOUND_404);
		ServiceResult<RequestResponse> semanticError = ServiceResult.empty(HttpStatus.UNPROCESSABLE_ENTITY_422);

		@Nullable Employee employee = this.repository.getEmployeeDao().get(loginID);
		if (employee == null)
		{
			LOGGER.debug("Bad request, invalid employee");
			return notFound;
		}

		@Nullable EventType eventType = this.repository.getEventTypeDao().get(json.getEventType());
		if (eventType == null)
		{
			LOGGER.debug("Bad request, invalid event type specified");
			return semanticError;
		}

		@Nullable String eventDate = json.getEventDate();
		Long eventDateMillis = eventDate == null ? null : ParseUtils.safeParseLong(eventDate, 0);
		String eventAddress = ParseUtils.emptyStringIfNull(json.getEventAddress());
		String eventDescription = ParseUtils.emptyStringIfNull(json.getEventDescription());

		String eventCost = ParseUtils.emptyStringIfNull(json.getEventCost());
		String[] dollarsAndCents = eventCost.split("[.]");
		int dollars = ParseUtils.safeParseInt(dollarsAndCents[0], 0);
		int cents = dollarsAndCents.length > 1 ? ParseUtils.safeParseInt(dollarsAndCents[1],0) : 0;
		int totalCents = dollars*100 + cents;
		if (totalCents <= 0)
		{
			LOGGER.debug("Bad request, event cost must be a valid dollar-and-cent amount (in american cents) strictly greater than zero");
			return semanticError;
		}

		int reimbursementAmount = this.getExpectedReimbursement(employee.getEmployeeID(), eventType, totalCents);
		if (reimbursementAmount <= 0)
		{
			LOGGER.debug("Request is unfundable, cannot be reimbursed");
			return semanticError;
		}

		int gradingFormatID = json.getGradingFormat();
		GradingFormat gradingFormat = this.repository.getGradingFormatDao().get(gradingFormatID);
		if (gradingFormat == null)
		{
			LOGGER.debug("Bad request, invalid grading format specified");
			return semanticError;
		}

		String passingGrade = ParseUtils.emptyStringIfNull(json.getPassingGrade());
		String workRelatedJustification = ParseUtils.emptyStringIfNull(json.getWorkRelatedJustification());

		long submissionTime = System.currentTimeMillis();

		String workTimeMissed = ParseUtils.emptyStringIfNull((json.getWorkTimeMissed()));

		ReimbursementRequest request = new ReimbursementRequest(
			employee,
			false,
			eventType,
			eventDateMillis,
			eventAddress,
			eventDescription,
			totalCents,
			reimbursementAmount,
			0,
			gradingFormat,
			passingGrade,
			workRelatedJustification,
			submissionTime,
			false,
			workTimeMissed
		);

		request = this.repository.getRequestDao().add(request);
		return ServiceResult.either(request, HttpStatus.CREATED_201, HttpStatus.INTERNAL_SERVER_ERROR_500)
			.map(this::getRequestResponse);
	}

	@Override
	public List<RequestResponse> getRequestsOwnedBy(int employeeID)
	{
		LOGGER.debug("Getting requests requested by {}", employeeID);
		return this.repository.getRequestsOwnedBy(employeeID)
			.stream()
			.map(this::getRequestResponse)
			.collect(Collectors.toList());
	}

	@Override
	public List<RequestResponse> getRequestsApprovableBy(int employeeID)
	{
		LOGGER.debug("Getting requests approvable by {}", employeeID);
		Employee approver = this.repository.getEmployeeDao().get(employeeID);
		if (approver == null)
			return Collections.emptyList();

		return this.repository.getRequestDao().getAll()
			.stream()
			.filter(request -> {
				RequestStatus status = getRequestStatus(request);
				if (status == RequestStatus.DENIED || status == RequestStatus.AWARDED)
					return false;
				ApprovalLevel approvalLevel = this.getApprovalLevel(request, approver);
				if (status == RequestStatus.NEEDS_PROOF_OF_COMPLETION)
				{
					GradingFormat format = request.getGradingFormat();
					if (format.isRequiresPresentation())
					{
						return request.getEmployee().getSupervisor().getEmployeeID() == approver.getEmployeeID();
					}
					else
					{
						return approvalLevel == ApprovalLevel.BENCO;
					}
				}
				return approvalLevel.getApprovableStatus() == status;
			})
			.map(this::getRequestResponse)
			.collect(Collectors.toList());
	}

	@Override
	public NewRequestFormResponse getNewRequestFormResponse(int employeeID)
	{
		int remainingReimbursement = this.getRemainingReimbursement(employeeID);
		List<EventType> eventTypes = this.repository.getEventTypeDao().getAll();
		List<GradingFormat> gradingFormats = this.repository.getGradingFormatDao().getAll();
		return new NewRequestFormResponse(remainingReimbursement, eventTypes, gradingFormats);
	}

	@Override
	public boolean deleteRequest(int loginID, int requestID)
	{
		// requests are only allowed to be deleted by the employee that requested them
		LOGGER.debug("Attempting to delete request {}", requestID);
		ReimbursementRequest request = this.repository.getRequestDao().get(requestID);
		if (request == null)
		{
			LOGGER.debug("Cannot delete request {} as it does not exist", requestID);
			return false;
		}
		if (request.getEmployee().getEmployeeID() != loginID)
		{
			LOGGER.debug("Cannot delete request {}, user lacks authorization", requestID);
			return false;
		}
		return this.repository.getRequestDao().delete(requestID);
	}

	@Nullable
	@Override
	public ServiceResult<ApprovalResponse> addApproval(int loginID, NewApprovalRequestJson json)
	{
		LOGGER.debug("Adding new approval");

		ServiceResult<ApprovalResponse> notFound = ServiceResult.empty(HttpStatus.NOT_FOUND_404);
		ServiceResult<ApprovalResponse> semanticError = ServiceResult.empty(HttpStatus.UNPROCESSABLE_ENTITY_422);


		int requestID = json.getRequestID();
		ReimbursementRequest request = this.repository.getRequestDao().get(requestID);
		if (request == null)
		{
			LOGGER.debug("Could not add new approval, request {} does not exist", requestID);
			return notFound;
		}

		Employee employee = this.repository.getEmployeeDao().get(loginID);
		if (employee == null)
		{
			LOGGER.debug("Could not add new approval, employee does not exist");
			return notFound;
		}

		ApprovalLevel approvalLevel = this.getApprovalLevel(request, employee);
		if (approvalLevel == ApprovalLevel.NONE)
		{
			LOGGER.debug("Could not add new approval, employee cannot approve the specified request");
			return semanticError;
		}

		long submissionTime = System.currentTimeMillis();

		Approval approval = new Approval(
			employee,
			request,
			approvalLevel,
			json.isApproved(),
			submissionTime,
			ParseUtils.emptyStringIfNull(json.getComments())
		);

		approval = this.repository.getApprovalDao().add(approval);
		return ServiceResult.either(approval, HttpStatus.CREATED_201, HttpStatus.INTERNAL_SERVER_ERROR_500)
			.map(this::getApprovalResponse);
	}

//	@Nullable
//	@Override
//	public ServiceResult<AttachmentResponse> addAttachment(int loginID, NewAttachmentRequestJson json)
//	{
//		LOGGER.debug("Adding new attachment");
//
//		int requestID = json.getRequestID();
//		@Nullable ReimbursementRequest request = this.repository.getRequestDao().get(requestID);
//		if (request == null)
//		{
//			LOGGER.debug("Could not add new approval, request {} does not exist", requestID);
//			return ServiceResult.empty(HttpStatus.NOT_FOUND_404);
//		}
//
//		String description = ParseUtils.emptyStringIfNull(json.getDescription());
//		@Nullable ApprovalLevel approvalLevel = json.getApprovalLevel();
//		boolean proofOfCompletion = json.isProofOfCompletion();
//
//		Attachment attachment = new Attachment(
//			request,
//			description,
//			"",
//			approvalLevel,
//			proofOfCompletion
//		);
//
//		attachment = this.repository.getAttachmentDao().add(attachment);
//
//		return ServiceResult.either(attachment, HttpStatus.CREATED_201, HttpStatus.INTERNAL_SERVER_ERROR_500)
//			.map(this::getAttachmentResponse);
//	}

	@Override
	public int getExpectedReimbursement(int employeeID, @NotNull EventType eventType, int eventCost)
	{
		int remainingReimbursement = this.getRemainingReimbursement(employeeID);
		int coveragePercentage = eventType.getCoveragePercentage();
		int coverableCost = (eventCost * coveragePercentage) / 100;
		return Math.min(remainingReimbursement, coverableCost);
	}

	@Override
	public ApprovalLevel getApprovalLevel(ReimbursementRequest request, @NotNull Employee approver)
	{
		boolean canCoordinateBenefits = approver.getRoles()
			.stream()
			.anyMatch(Role::canCoordinateBenefits);
		if (canCoordinateBenefits)
			return ApprovalLevel.BENCO;

		Employee requestor = request.getEmployee();
		int approverID = approver.getEmployeeID();
		if (requestor.getDepartment() != null && requestor.getDepartment().getDepartmentHead() != null && requestor.getDepartment().getDepartmentHead().getEmployeeID() == approverID)
			return ApprovalLevel.DEPARTMENT;

		if (requestor.getSupervisor() != null && requestor.getSupervisor().getEmployeeID() == approverID)
			return ApprovalLevel.SUPERVISOR;

		return ApprovalLevel.NONE;
	}

	@Override
	public List<ReimbursementRequest> getAwardedOrPendingRequests(int employeeID)
	{
		// TODO do all of the filtering here in a database query
		return this.repository.getRequestsOwnedBy(employeeID)
			.stream()
			.filter(request -> request.isAwarded()
				||
				this.repository.getApprovalsForRequests(request.getRequestID())
					.stream()
					.filter(approval -> !approval.isApproved())
					.count() == 0)
			.collect(Collectors.toList());
	}

	private EmployeeResponse getEmployeeResponse(@NotNull Employee employee)
	{
		return new EmployeeResponse(employee.getName());
	}

	private RequestResponse getRequestResponse(@NotNull ReimbursementRequest request)
	{
		Employee employee = request.getEmployee();
		Long eventDate = request.getEventDate();
		EventType eventType = request.getEventType();
		GradingFormat gradingFormat = request.getGradingFormat();
		String status = getRequestStatus(request).toString();
		List<ApprovalResponse> approvals = this.repository.getApprovalsForRequests(request.getRequestID())
			.stream()
			.map(this::getApprovalResponse)
			.collect(Collectors.toList());
		return new RequestResponse(
			request.getRequestID(),
			employee.getEmployeeID(),
			employee.getName(),
			eventDate == null ? null : String.valueOf(eventDate),
			String.valueOf(request.getSubmissionTime()),
			status,
			eventType.getEventTypeID(),
			eventType.getName(),
			request.getEventAddress(),
			request.getEventDescription(),
			centsToDollarsAndCents(request.getEventCost()),
			centsToDollarsAndCents(request.getReimbursementAmount()),
			gradingFormat.getFormatID(),
			gradingFormat.getName(),
			request.getPassingGrade(),
			request.getWorkRelatedJustification(),
			request.getWorkTimeMissed(),
			request.isRequestorMustReviewModifications(),
			approvals
		);
	}

	@Override
	public RequestStatus getRequestStatus(ReimbursementRequest request)
	{
		if (request.isAwarded())
			return RequestStatus.AWARDED;
		List<Approval> approvals = this.repository.getApprovalsForRequests(request.getRequestID());
		Employee employee = request.getEmployee();
		Employee supervisor = employee.getSupervisor();
		Department department = employee.getDepartment();
		RequestStatus status = supervisor == null || supervisor.getEmployeeID() == employee.getEmployeeID()
			? department == null || department.getDepartmentHead().getEmployeeID() == employee.getEmployeeID()
				? RequestStatus.NEEDS_BENEFITS_COORDINATOR_APPROVAL
				: RequestStatus.NEEDS_DEPARTMENT_APPROVAL
			: RequestStatus.NEEDS_SUPERVISOR_APPROVAL;

		for (Approval approval : approvals)
		{
			if (!approval.isApproved())
			{
				status = RequestStatus.DENIED;
				break;
			}
			ApprovalLevel level = approval.getApprovalLevel();
			RequestStatus nextStatus = level.getNextStatus();
			if (nextStatus.ordinal() > status.ordinal())
			{
				status = nextStatus;
			}
		}

		return status;
	}

	@Override
	public int awardRequest(int employeeID, int requestID)
	{
		LOGGER.debug("Processing approving employee {}'s award to request {}", employeeID, requestID);
		@Nullable Employee approver = this.repository.getEmployeeDao().get(employeeID);
		if (approver == null)
		{
			LOGGER.debug("Approving employee {} not found, returning status 404", employeeID);
			return HttpStatus.NOT_FOUND_404;
		}
		@Nullable ReimbursementRequest request = this.repository.getRequestDao().get(requestID);
		if (request == null)
		{
			LOGGER.debug("Request {} not found, returning status 404", requestID);
			return HttpStatus.NOT_FOUND_404;
		}
		if (this.getRequestStatus(request) != RequestStatus.NEEDS_PROOF_OF_COMPLETION)
		{
			LOGGER.debug("No request {} exists that can be awarded, returning status 404", requestID);
			return HttpStatus.NOT_FOUND_404;
		}
		boolean requiresPresentation = request.getGradingFormat().isRequiresPresentation();
		if (requiresPresentation && request.getEmployee().getSupervisor().getEmployeeID() == approver.getEmployeeID()
			|| !requiresPresentation && isEmployeeBenefitsCoordinator(approver))
		{
			request.setAwarded(true);
			@Nullable ReimbursementRequest updatedRequest = this.repository.getRequestDao().update(request);
			if (updatedRequest == null)
			{
				LOGGER.debug("Could not update request {}, responding with status 404", requestID);
				return HttpStatus.NOT_FOUND_404;
			}
			LOGGER.debug("Successfully awarded request {}", requestID);
			return HttpStatus.OK_200;
		}
		else
		{
			LOGGER.debug("No request {} exists that can be awarded by employee {}, responding with 404", requestID, employeeID);
			return HttpStatus.NOT_FOUND_404;
		}
	}

	private boolean isEmployeeBenefitsCoordinator(@NotNull Employee employee)
	{
		return employee.getRoles()
			.stream()
			.filter(Role::canCoordinateBenefits)
			.count() > 0;
	}

	private ApprovalResponse getApprovalResponse(@NotNull Approval approval)
	{
		return new ApprovalResponse(
			approval.getApprover().getName(),
			approval.getApprovalLevel(),
			approval.isApproved(),
			approval.getTime(),
			approval.getComments()
		);
	}

//	private AttachmentResponse getAttachmentResponse(@NotNull Attachment attachment)
//	{
//		return new AttachmentResponse();
//	}

	private String centsToDollarsAndCents(int totalCents)
	{
		int dollars = totalCents / 100;
		int cents = totalCents % 100;
		return String.valueOf(dollars) + "." + String.valueOf(cents);
	}
}
