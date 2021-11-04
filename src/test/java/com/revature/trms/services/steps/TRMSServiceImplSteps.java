package com.revature.trms.services.steps;

import com.revature.trms.models.Approval;
import com.revature.trms.models.Employee;
import com.revature.trms.models.GradingFormat;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.repositories.CrudDao;
import com.revature.trms.repositories.TRMSDao;
import com.revature.trms.requestmodels.NewApprovalRequestJson;
import com.revature.trms.requestmodels.NewRequestRequestJson;
import com.revature.trms.responsemodels.EmployeeResponse;
import com.revature.trms.responsemodels.NewRequestFormResponse;
import com.revature.trms.responsemodels.RequestResponse;
import com.revature.trms.services.TRMSService;
import com.revature.trms.services.TRMSServiceImpl;
import com.revature.trms.services.TRMSServiceImplRunner;
import com.revature.trms.util.ApprovalLevel;
import com.revature.trms.util.ServiceResult;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TRMSServiceImplSteps
{
	private int employeeID;
	private String name;
	private String username;
	private String password;
	private int secondEmployeeID;
	private NewRequestRequestJson newRequestRequestJson;
	private int pendingRequestID;

	private int supervisorID;
	private int departmentHeadID;
	private int benefitsCoordinatorID;

	private Employee actualEmployee;
	private EmployeeResponse actualEmployeeResponse;
	private int actualRemainingReimbursement;
	private ServiceResult<RequestResponse> actualRequestResponseResult;
	private List<RequestResponse> actualRequestResponses;
	private boolean requestDeleted;
	private int awardResponse;
	private NewRequestFormResponse newRequestFormResponse;

	@Given("An employee exists")
	public void anEmployeeExists()
	{
		Random rand = TRMSServiceImplRunner.RANDOM;
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		Employee expectedEmployee = null;
		while (expectedEmployee == null)
		{
			// usernames must be unique
			// so this will fail if username already exists
			this.name = String.valueOf(rand.nextInt(10000));
			this.username = String.valueOf(rand.nextInt(10000));
			this.password = String.valueOf(rand.nextInt(10000));
			Employee newEmployeeData = new Employee(this.name, this.username, this.password, null, dao.getDepartmentDao().get(1), Collections.emptyList());
			expectedEmployee = dao.getEmployeeDao().add(newEmployeeData);
		}
		this.employeeID = expectedEmployee.getEmployeeID();
		// set the "second employee ID" to the first's,
		// so that we *must* change it if a second employee is used
		this.secondEmployeeID = this.employeeID;
		TRMSServiceImplRunner.CREATED_EMPLOYEES.add(this.employeeID);
	}

	@Given("The employee was deleted")
	public void theEmployeeWasDeleted()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		dao.getEmployeeDao().delete(this.employeeID);
	}

	@Given("A second employee exists")
	public void aSecondEmployeeExists()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		Employee newEmployeeDataB = new Employee(this.name + "2", this.username + "2", this.password + "2", null, null, Collections.emptyList());
		newEmployeeDataB = dao.getEmployeeDao().add(newEmployeeDataB);
		this.secondEmployeeID = newEmployeeDataB.getEmployeeID();
		TRMSServiceImplRunner.CREATED_EMPLOYEES.add(this.secondEmployeeID);
	}

	@Given("The employee has submitted {int} requests for {int} dollars each recently")
	public void theEmployeeHasSubmittedRequestsForDollarsEachRecently(int requests, int dollars)
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		for (int i=0; i<requests; i++)
		{
			int amount = dollars*100; // stored in american cents
			ReimbursementRequest request = new ReimbursementRequest();
			request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
			request.setEventType(dao.getEventTypeDao().get(1));
			request.setEventCost(amount);
			request.setReimbursementAmount(amount);
			GradingFormat format = dao.getGradingFormatDao().get(1);
			request.setGradingFormat(format);
			request.setPassingGrade(format.getDefaultPassingGrade());
			request.setSubmissionTime(System.currentTimeMillis());
			request = dao.getRequestDao().add(request);
			TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		}
	}

	@Given("The employee has submitted {int} requests for {int} dollars each {int} days ago")
	public void theEmployeeHasSubmittedStaleNumberRequestsForStaleAmountDollarsEachDaysAgo(int requests, int dollars, int days)
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		for (int i=0; i<requests; i++)
		{
			int amount = dollars*100; // stored in american cents
			ReimbursementRequest request = new ReimbursementRequest();
			request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
			request.setEventType(dao.getEventTypeDao().get(1));
			request.setEventCost(amount);
			request.setReimbursementAmount(amount);
			GradingFormat format = dao.getGradingFormatDao().get(1);
			request.setGradingFormat(format);
			request.setPassingGrade(format.getDefaultPassingGrade());
			long time = System.currentTimeMillis() - (1000L * 60L * 60L * 24L * (long)days);
			request.setSubmissionTime(time);
			request = dao.getRequestDao().add(request);
			TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		}
	}

	@Given("The most recent request was denied or not? It was {string}")
	public void theMostRecentRequestWasDeniedOrNotItWas(String deniedOrNot)
	{
		if (Objects.equals("denied", deniedOrNot))
		{
			TRMSDao dao = TRMSServiceImplRunner.DAO;
			TRMSService service = TRMSServiceImplRunner.SERVICE;
			dao.getRequestsOwnedBy(this.employeeID)
				.stream()
				.reduce((a,b)-> a.getSubmissionTime() > b.getSubmissionTime() ? a : b)
				.ifPresent(request ->{
					Approval approval = new Approval();
					approval.setApprovalLevel(ApprovalLevel.BENCO);
					approval.setApproved(false);
					approval.setApprover(dao.getEmployeeDao().get(1));
					approval.setRequest(request);
					dao.getApprovalDao().add(approval);
					TRMSServiceImplRunner.CREATED_APPROVALS.add(approval.getApprovalID());
				});

		}
	}

	@Given("Employee desires to request a new reimbursement request")
	public void employeeDesiresToRequestANewReimbursementRequest()
	{
		this.newRequestRequestJson = new NewRequestRequestJson(
			1,
			"1635468084000",
			"Minneapolis, MN",
			"VR Accounting",
			"19.99",
			1,
			"Definitely work related",
			"Pass",
			"N/A"
		);
	}

	@Given("Request has bad event type")
	public void requestHasBadEventType()
	{
		this.newRequestRequestJson.setEventType(0);
	}

	@Given("Request has bad event cost")
	public void requestHasBadEventCost()
	{
		this.newRequestRequestJson.setEventCost("0");
	}

	@Given("The employee has no remaining reimbursements due to pending requests")
	public void theEmployeeHasNoRemainingReimbursementsDueToPendingRequests()
	{
		NewRequestRequestJson existingRequestJson = new NewRequestRequestJson(
			1,
			String.valueOf(System.currentTimeMillis()),
			"Minneapolis",
			"Angular Improvement",
			"100000",
			1,
			"Yes",
			"Pass",
			"N/A");
		TRMSServiceImplRunner.SERVICE.addRequest(this.employeeID,existingRequestJson);
	}

	@Given("Request has bad grading format")
	public void theRequestHasBadGradingFormat()
	{
		this.newRequestRequestJson.setGradingFormat(0);
	}

	@Given("Employee has three reimbursement requests")
	public void employeeHasThreeReimbursementRequests()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		for (int i=0; i<3; i++)
		{
			int amount = 1; // stored in american cents
			ReimbursementRequest request = new ReimbursementRequest();
			request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
			request.setEventType(dao.getEventTypeDao().get(1));
			request.setEventCost(amount);
			request.setReimbursementAmount(amount);
			GradingFormat format = dao.getGradingFormatDao().get(1);
			request.setGradingFormat(format);
			request.setPassingGrade(format.getDefaultPassingGrade());
			long time = System.currentTimeMillis();
			request.setSubmissionTime(time);
			request = dao.getRequestDao().add(request);
			TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		}
	}

	@Given("Employee has a pending request")
	public void employeeHasAPendingRequest()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		int amount = 1; // stored in american cents
		ReimbursementRequest request = new ReimbursementRequest();
		request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
		request.setEventType(dao.getEventTypeDao().get(1));
		request.setEventCost(amount);
		request.setReimbursementAmount(amount);
		GradingFormat format = dao.getGradingFormatDao().get(1);
		request.setGradingFormat(format);
		request.setPassingGrade(format.getDefaultPassingGrade());
		long time = System.currentTimeMillis();
		request.setSubmissionTime(time);
		request = dao.getRequestDao().add(request);
		TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		this.pendingRequestID = request.getRequestID();
	}

	@Given("The request was deleted")
	public void theRequestIsDeleted()
	{
		TRMSServiceImplRunner.DAO.getRequestDao().delete(this.pendingRequestID);
	}

	@Given("A supervisor exists")
	public void aSupervisorExists()
	{
		CrudDao<Employee> employeeDao = TRMSServiceImplRunner.DAO.getEmployeeDao();
		Employee supervisor = employeeDao.add(new Employee(
			"Supervisor Bob",
			"supervisorbob",
			"password",
			null,
			null,
			Collections.emptyList()
		));
		this.supervisorID = supervisor.getEmployeeID();
		TRMSServiceImplRunner.CREATED_EMPLOYEES.add(this.supervisorID);

		Employee thisEmployee = employeeDao.get(this.employeeID);
		thisEmployee.setSupervisor(supervisor);
		employeeDao.update(thisEmployee);
	}

	@Given("A department head exists")
	public void aDepartmentHeadExists()
	{
		this.departmentHeadID = TRMSServiceImplRunner.DAO.getEmployeeDao().get(this.employeeID).getDepartment().getDepartmentHead().getEmployeeID();
	}

	@Given("A benefits coordinator exists")
	public void aBenefitsCoordinatorExists()
	{
		// just hardcode it to the test benefits coordinator
		this.benefitsCoordinatorID = 9;
	}

	@Given("A request for employee exists that does not require a presentation")
	public void aRequestForEmployeeExistsThatDoesNotRequireAPresentation()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		int amount = 1; // stored in american cents
		ReimbursementRequest request = new ReimbursementRequest();
		request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
		request.setEventType(dao.getEventTypeDao().get(1));
		request.setEventCost(amount);
		request.setReimbursementAmount(amount);
		GradingFormat format = dao.getGradingFormatDao().get(1);
		request.setGradingFormat(format);
		request.setPassingGrade(format.getDefaultPassingGrade());
		long time = System.currentTimeMillis();
		request.setSubmissionTime(time);
		request = dao.getRequestDao().add(request);
		TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		this.pendingRequestID = request.getRequestID();
	}

	@Given("A request for employee exists that requires a presentation")
	public void aRequestForEmployeeExistsThatRequiresAPresentation()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		int amount = 1; // stored in american cents
		ReimbursementRequest request = new ReimbursementRequest();
		request.setEmployee(dao.getEmployeeDao().get(this.employeeID));
		request.setEventType(dao.getEventTypeDao().get(1));
		request.setEventCost(amount);
		request.setReimbursementAmount(amount);
		GradingFormat format = dao.getGradingFormatDao().get(5); // hardcode it to the test database's format for now
		request.setGradingFormat(format);
		request.setPassingGrade(format.getDefaultPassingGrade());
		long time = System.currentTimeMillis();
		request.setSubmissionTime(time);
		request = dao.getRequestDao().add(request);
		TRMSServiceImplRunner.CREATED_REQUESTS.add(request.getRequestID());
		this.pendingRequestID = request.getRequestID();
	}

	@Given("Supervisor has approved request")
	public void supervisorHasApprovedRequest()
	{
		TRMSServiceImplRunner.SERVICE.addApproval(this.supervisorID, new NewApprovalRequestJson(
			this.pendingRequestID, true, ""
		));
	}

	@Given("Department head has approved request")
	public void departmentHeadHasApprovedRequest()
	{
		TRMSServiceImplRunner.SERVICE.addApproval(this.departmentHeadID, new NewApprovalRequestJson(
			this.pendingRequestID, true, ""
		));
	}

	@Given("Benefits coordinator has approved request")
	public void benefitsCoordinatorHasApprovedRequest()
	{
		TRMSServiceImplRunner.SERVICE.addApproval(this.benefitsCoordinatorID, new NewApprovalRequestJson(
			this.pendingRequestID, true, ""
		));
	}

	@Given("Benefits coordinator has awarded request")
	public void benefitsCoordinatorHasAwardedRequest()
	{
		TRMSServiceImplRunner.SERVICE.awardRequest(this.benefitsCoordinatorID, this.pendingRequestID);
	}

	@Given("Supervisor has awarded request")
	public void supervisorHasAwardedRequest()
	{
		TRMSServiceImplRunner.SERVICE.awardRequest(this.supervisorID, this.pendingRequestID);
	}

	@When("The employee's username and password are used")
	public void anEmployeesUsernameAndPasswordAreUsed()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualEmployee = service.getEmployeeByLogin(this.username, this.password);
	}

	@When("A correct username and incorrect password are used")
	public void aCorrectUsernameAndIncorrectPasswordAreUsed()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		String username = this.username;
		String password = this.password + "Not a real password";
		this.actualEmployee = service.getEmployeeByLogin(username, password);
	}

	@When("An incorrect username and correct password are used")
	public void anIncorrectUsernameAndCorrectPasswordAreUsed()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		String username = this.username + "Not a real username";
		String password = this.password;
		this.actualEmployee = service.getEmployeeByLogin(username, password);
	}

	@When("An incorrect username and password are used")
	public void anIncorrectUsernameIsUsed()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		String username = this.username + "Not a real username";
		String password = this.password + "Not a real password";
		this.actualEmployee = service.getEmployeeByLogin(username, password);
	}

	@When("The employee uses their own id to get employee response")
	public void theEmployeeUsesTheirOwnIdToGetEmployeeResponse()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualEmployeeResponse = service.getEmployeeResponse(this.employeeID, this.employeeID);
	}

	@When("The employee uses the other employee's id to get employee response")
	public void theEmployeeUsesTheOtherEmployeesIdToGetEmployeeResponse()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualEmployeeResponse = service.getEmployeeResponse(this.employeeID, this.secondEmployeeID);
	}

	@When("Remaining reimbursement is retrieved")
	public void remainingReimbursementIsRetrieved()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualRemainingReimbursement = service.getRemainingReimbursement(this.employeeID);
	}

	@When("Request added for employee")
	public void requestAddedForEmployee()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualRequestResponseResult = service.addRequest(this.employeeID, this.newRequestRequestJson);
	}

	@When("Employee gets owned requests")
	public void employeeGetsOwnedRequests()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.actualRequestResponses = service.getRequestsOwnedBy(this.employeeID);
	}

	@When("Employee deletes request")
	public void employeeDeletesRequest()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.requestDeleted = service.deleteRequest(this.employeeID, this.pendingRequestID);
	}

	@When("Second employee deletes first employee's request")
	public void secondEmployeeDeletesFirstEmployeesRequst()
	{
		TRMSService service = TRMSServiceImplRunner.SERVICE;
		this.requestDeleted = service.deleteRequest(this.secondEmployeeID, this.pendingRequestID);
	}

	@When("Employee gets approvable requests")
	public void employeeGetsApprovableRequests()
	{
		this.actualRequestResponses = TRMSServiceImplRunner.SERVICE.getRequestsApprovableBy(this.employeeID);
	}

	@When("Supervisor gets approvable requests")
	public void supervisorGetsApprovableRequests()
	{
		this.actualRequestResponses = TRMSServiceImplRunner.SERVICE.getRequestsApprovableBy(this.supervisorID);
	}

	@When("Department head gets approvable requests")
	public void departmentHeadGetsApprovableRequests()
	{
		this.actualRequestResponses = TRMSServiceImplRunner.SERVICE.getRequestsApprovableBy(this.departmentHeadID);
	}

	@When("Benefits coordinator gets approvable requests")
	public void benefitsCoordinatorGetsApprovableRequests()
	{
		this.actualRequestResponses = TRMSServiceImplRunner.SERVICE.getRequestsApprovableBy(this.benefitsCoordinatorID);
	}

	@When("Employee awards request")
	public void employeeAwardsRequest()
	{
		this.awardResponse = TRMSServiceImplRunner.SERVICE.awardRequest(this.employeeID, this.pendingRequestID);
	}

	@When("Supervisor awards request")
	public void supervisorAwardsRequest()
	{
		this.awardResponse = TRMSServiceImplRunner.SERVICE.awardRequest(this.supervisorID, this.pendingRequestID);
	}

	@When("Benefits coordinator awards request")
	public void benefitsCoordinatorAwardsRequest()
	{
		this.awardResponse = TRMSServiceImplRunner.SERVICE.awardRequest(this.benefitsCoordinatorID, this.pendingRequestID);
	}

	@When("Employee gets new request form response")
	public void employeeGetsNewRequestFormResponse()
	{
		this.newRequestFormResponse = TRMSServiceImplRunner.SERVICE.getNewRequestFormResponse(this.employeeID);
	}

	@Then("The correct employee is retrieved")
	public void theCorrectEmployeeIsRetrieved()
	{
		Assert.assertEquals(this.name, this.actualEmployee.getName());
		Assert.assertEquals(this.username, this.actualEmployee.getUsername());
		Assert.assertEquals(this.password, this.actualEmployee.getPassword());
	}

	@Then("No employee is retrieved")
	public void noEmployeeIsRetrieved()
	{
		Assert.assertNull(this.actualEmployee);
	}

	@Then("The employee response is retrieved")
	public void theEmployeeResponseIsRetrieved()
	{
		Assert.assertEquals(this.name, this.actualEmployeeResponse.getName());
	}

	@Then("No employee response is retrieved")
	public void noEmployeeResponseIsRetrieved()
	{
		Assert.assertNull(this.actualEmployeeResponse);
	}

	@Then("Remaining reimbursement is {int} dollars")
	public void remainingReimbursementIsIntDollars(int remainingReimbursement)
	{
		// reimbursement is stored in american cents
		Assert.assertEquals(remainingReimbursement*100, this.actualRemainingReimbursement);
	}

	@Then("The request response is correct")
	public void theRequestResponseIsCorrect()
	{
		int status = this.actualRequestResponseResult.getStatus();
		RequestResponse response = this.actualRequestResponseResult.getValue();
		Assertions.assertEquals(status, HttpStatus.CREATED_201);
		Assertions.assertEquals(this.employeeID, response.getEmployeeID());
		Assertions.assertEquals(this.name, response.getEmployeeName());
		Assertions.assertEquals(this.newRequestRequestJson.getEventDate(), response.getEventTime());
		Assertions.assertEquals(this.newRequestRequestJson.getEventType(), response.getEventTypeID());
		Assertions.assertEquals(TRMSServiceImplRunner.DAO.getEventTypeDao().get(
			this.newRequestRequestJson.getEventType()).getName(),
			response.getEventTypeName());
		Assertions.assertEquals(this.newRequestRequestJson.getEventAddress(), response.getEventAddress());
		Assertions.assertEquals(this.newRequestRequestJson.getEventDescription(), response.getEventDescription());
		double expectedEventCost = Double.parseDouble(this.newRequestRequestJson.getEventCost());
		double actualEventCost = Double.parseDouble(response.getEventCost());
		Assertions.assertTrue(expectedEventCost >= actualEventCost);
		double actualReimbursement = Double.parseDouble(response.getReimbursementAmount());
		Assertions.assertTrue(expectedEventCost >= actualReimbursement);
		Assertions.assertEquals(this.newRequestRequestJson.getPassingGrade(), response.getPassingGrade());
		Assertions.assertEquals(this.newRequestRequestJson.getWorkTimeMissed(), response.getWorkTimeMissed());
	}

	@Then("The request response is bad request")
	public void theRequestResponseIsBadRequest()
	{
		Assertions.assertEquals(HttpStatus.BAD_REQUEST_400, this.actualRequestResponseResult.getStatus());
		Assertions.assertNull(this.actualRequestResponseResult.getValue());
	}

	@Then("The request response status is not found")
	public void theRequestResponseStatusIsNotFound()
	{
		Assertions.assertEquals(HttpStatus.NOT_FOUND_404, this.actualRequestResponseResult.getStatus());
		Assertions.assertNull(this.actualRequestResponseResult.getValue());
	}

	@Then("The request response status is unprocessable entity")
	public void theRequestResponseStatusIsUnprocessableEntity()
	{
		Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, this.actualRequestResponseResult.getStatus());
		Assertions.assertNull(this.actualRequestResponseResult.getValue());
	}

	@Then("Three reimbursement requests are retrieved")
	public void threeRequestsAreRetrieved()
	{
		Assertions.assertEquals(3, this.actualRequestResponses.size());
	}

	@Then("Request has been deleted")
	public void requestHasBeenDeleted()
	{
		Assert.assertTrue(this.requestDeleted);
	}

	@Then("Request still exists")
	public void requestStillExists()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		ReimbursementRequest request = dao.getRequestDao().get(this.pendingRequestID);
		Assertions.assertNotNull(request);
	}

	@Then("Request is not deleted")
	public void requestIsNotDeleted()
	{
		Assert.assertFalse(this.requestDeleted);
	}

	@Then("Request does not exist")
	public void requestDoesNotExist()
	{
		TRMSDao dao = TRMSServiceImplRunner.DAO;
		ReimbursementRequest request = dao.getRequestDao().get(this.pendingRequestID);
		Assertions.assertNull(request);
	}

	@Then("The request is not in the approvable requests")
	public void theRequestIsNotInTheApprovableRequests()
	{
		boolean requestIsApprovable = this.actualRequestResponses.stream()
			.map(RequestResponse::getRequestID)
			.anyMatch(i -> i==this.pendingRequestID);
		Assertions.assertFalse(requestIsApprovable);
	}

	@Then("The request is in the approvable requests")
	public void theRequestIsInTheApprovableRequests()
	{
		boolean requestIsApprovable = this.actualRequestResponses.stream()
			.map(RequestResponse::getRequestID)
			.anyMatch(i -> i==this.pendingRequestID);
		Assertions.assertTrue(requestIsApprovable);
	}

	@Then("Award response is not found")
	public void awardResponseIsNotFound()
	{
		Assertions.assertEquals(HttpStatus.NOT_FOUND_404, this.awardResponse);
	}

	@Then("Award response is success")
	public void awardResponseIsSuccess()
	{
		Assertions.assertEquals(HttpStatus.OK_200, awardResponse);
	}

	@Then("Request has been awarded")
	public void requestHasBeenAwarded()
	{
		Assertions.assertTrue(TRMSServiceImplRunner.DAO.getRequestDao().get(this.pendingRequestID).isAwarded());
	}

	@Then("Request has not been awarded")
	public void requestHasNotBeenAwarded()
	{
		Assertions.assertFalse(TRMSServiceImplRunner.DAO.getRequestDao().get(this.pendingRequestID).isAwarded());
	}

	@Then("New request form response has one thousand dollars remaining")
	public void newRequestFormResponseHasOneThousandDollarsRemaining()
	{
		Assertions.assertEquals(1000*100, this.newRequestFormResponse.getRemainingReimbursement());
	}
}
