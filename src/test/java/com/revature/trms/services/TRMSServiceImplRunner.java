package com.revature.trms.services;

import com.revature.trms.app.App;
import com.revature.trms.models.Approval;
import com.revature.trms.models.Attachment;
import com.revature.trms.models.Department;
import com.revature.trms.models.Employee;
import com.revature.trms.models.EventType;
import com.revature.trms.models.GradingFormat;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.models.Role;
import com.revature.trms.repositories.HibernateCrudDao;
import com.revature.trms.repositories.HibernateTRMSDao;
import com.revature.trms.repositories.TRMSDao;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/trms/services", glue="com.revature.trms.services.steps")
public class TRMSServiceImplRunner
{
	public static final Random RANDOM = new Random(System.currentTimeMillis());
	public static final Set<Integer> CREATED_EMPLOYEES = new HashSet<>();
	public static final Set<Integer> CREATED_REQUESTS = new HashSet<>();
	public static final Set<Integer> CREATED_APPROVALS = new HashSet<>();
	public static final TRMSDao DAO = makeDao();
	public static final TRMSService SERVICE = makeService(DAO);

	@BeforeClass
	public static void beforeClass()
	{

	}

	@AfterClass
	public static void afterClass()
	{
		TRMSDao dao = makeDao();
		CREATED_EMPLOYEES.forEach(id -> dao.getEmployeeDao().delete(id));
		CREATED_REQUESTS.forEach(id -> dao.getRequestDao().delete(id));
		CREATED_APPROVALS.forEach(id -> dao.getApprovalDao().delete(id));

	}

	private static TRMSDao makeDao()
	{
		return new HibernateTRMSDao(
			new HibernateCrudDao<>(Approval.class, "approvalID", Approval::setApprovalID),
			new HibernateCrudDao<>(Attachment.class, "attachmentID", Attachment::setAttachmentID),
			new HibernateCrudDao<>(Department.class, "departmentID", Department::setDepartmentID),
			new HibernateCrudDao<>(Employee.class, "employeeID", Employee::setEmployeeID),
			new HibernateCrudDao<>(EventType.class, "eventTypeID", EventType::setEventTypeID),
			new HibernateCrudDao<>(GradingFormat.class, "formatID", GradingFormat::setFormatID),
			new HibernateCrudDao<>(ReimbursementRequest.class, "requestID", ReimbursementRequest::setRequestID),
			new HibernateCrudDao<>(Role.class, "roleID", Role::setRoleID));
	}

	private static TRMSService makeService(TRMSDao dao)
	{
		Properties props = new Properties();
		try (InputStream input = App.class.getClassLoader().getResourceAsStream("trms.properties"))
		{
			props.load(input);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String hostURL = props.getProperty("hostURL");
		return new TRMSServiceImpl(dao, hostURL);
	}
}
