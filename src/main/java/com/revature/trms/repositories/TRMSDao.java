package com.revature.trms.repositories;

import com.revature.trms.models.Approval;
import com.revature.trms.models.Attachment;
import com.revature.trms.models.Department;
import com.revature.trms.models.Employee;
import com.revature.trms.models.EventType;
import com.revature.trms.models.GradingFormat;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.models.Role;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Data Access Object for doing complex database queries and operations
 * on the TRMS database tables (beyond basic CRUD)
 */
public interface TRMSDao
{
	// sub-repositories for basic CRUD operations
	public CrudDao<Approval> getApprovalDao();
	public CrudDao<Attachment> getAttachmentDao();
	public CrudDao<Department> getDepartmentDao();
	public CrudDao<Employee> getEmployeeDao();
	public CrudDao<EventType> getEventTypeDao();
	public CrudDao<GradingFormat> getGradingFormatDao();
	public CrudDao<ReimbursementRequest> getRequestDao();
	public CrudDao<Role> getRoleDao();

	// complex requests
	@Nullable
	public Employee getEmployee(String username, String password);
	List<ReimbursementRequest> getRequestsOwnedBy(int employeeID);
	List<Approval> getApprovalsForRequests(int requestID);
}
