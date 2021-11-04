package com.revature.trms.services;

import com.revature.trms.models.Employee;
import com.revature.trms.models.EventType;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.requestmodels.NewApprovalRequestJson;
import com.revature.trms.requestmodels.NewAttachmentRequestJson;
import com.revature.trms.requestmodels.NewRequestRequestJson;
import com.revature.trms.responsemodels.ApprovalResponse;
import com.revature.trms.responsemodels.AttachmentResponse;
import com.revature.trms.responsemodels.EmployeeResponse;
import com.revature.trms.responsemodels.NewRequestFormResponse;
import com.revature.trms.responsemodels.RequestResponse;
import com.revature.trms.util.ApprovalLevel;
import com.revature.trms.util.RequestStatus;
import com.revature.trms.util.ServiceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.ws.Service;
import java.util.List;

public interface TRMSService
{
	@Nullable
	public Employee getEmployeeByLogin(String username, String password);
	@Nullable
	public EmployeeResponse getEmployeeResponse(int loginID, int employeeID);
	public int getRemainingReimbursement(int employeeID);
	public ServiceResult<RequestResponse> addRequest(int loginID, NewRequestRequestJson json);
	public List<RequestResponse> getRequestsOwnedBy(int employeeID);
	public List<RequestResponse> getRequestsApprovableBy(int employeeID);
	public NewRequestFormResponse getNewRequestFormResponse(int employeeID);
	public boolean deleteRequest(int loginID, int requestID);
	public ServiceResult<ApprovalResponse> addApproval(int loginID, NewApprovalRequestJson approval);
//	public ServiceResult<AttachmentResponse> addAttachment(int loginID, NewAttachmentRequestJson attachment);

	public int getExpectedReimbursement(int employeeID, @NotNull EventType eventType, int eventCost);
	public ApprovalLevel getApprovalLevel(ReimbursementRequest request, Employee approver);
	public List<ReimbursementRequest> getAwardedOrPendingRequests(int employeeID);
	public RequestStatus getRequestStatus(ReimbursementRequest request);
	public int awardRequest(int employeeID, int requestID);
}
