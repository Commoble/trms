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
import com.revature.trms.responsemodels.EmployeeResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

public class TRMSServiceImplTests
{
//	public static final Random RANDOM = new Random(System.currentTimeMillis());
//
//	public static final Set<Integer> CREATED_EMPLOYEES = new HashSet<>();
//
//	@BeforeAll
//	public static void beforeAll()
//	{
//
//	}
//
//	@AfterAll
//	public static void afterAll()
//	{
//		TRMSDao dao = makeDao();
//		CREATED_EMPLOYEES.forEach(id -> dao.getEmployeeDao().delete(id));
//	}
//
//	@Test
//	void getEmployeeGetsEmployee()
//	{
//		TRMSDao dao = makeDao();
//		TRMSService service = makeService(dao);
//		// create an employee so we know it exists
//		// then try to get it
//		String name = String.valueOf(RANDOM.nextInt(10000));
//		String username = String.valueOf(RANDOM.nextInt(10000));
//		String password = String.valueOf(RANDOM.nextInt(10000));
//		Employee newEmployeeData = new Employee(name, username, password, null, null, Collections.emptyList());
//		newEmployeeData = dao.getEmployeeDao().add(newEmployeeData);
//		CREATED_EMPLOYEES.add(newEmployeeData.getEmployeeID());
//
//		Employee retrievedEmployee = service.getEmployeeByLogin(username, password);
//		Assertions.assertEquals(username, retrievedEmployee.getUsername());
//	}
//
//	@Test
//	void getEmployeeDoesntGetMissingEmployee()
//	{
//		TRMSDao dao = makeDao();
//		TRMSService service = makeService(dao);
//		// create an employee, then delete it, then try to get that employee
//		Employee newEmployee = null;
//		// creates will fail if employee username is already in use for some reason
//		while (newEmployee == null)
//		{
//			String name = String.valueOf(RANDOM.nextInt(10000));
//			String username = String.valueOf(RANDOM.nextInt(10000));
//			String password = String.valueOf(RANDOM.nextInt(10000));
//			Employee newEmployeeData = new Employee(name, username, password, null, null, Collections.emptyList());
//			newEmployee = dao.getEmployeeDao().add(newEmployeeData);
//		}
//		int id = newEmployee.getEmployeeID();
//		String username = newEmployee.getUsername();
//		String password = newEmployee.getPassword();
//		CREATED_EMPLOYEES.add(id);
//		dao.getEmployeeDao().delete(id);
//		Employee retrievedEmployee = service.getEmployeeByLogin(username, password);
//		Assertions.assertNull(retrievedEmployee);
//	}
//
//	@Test
//	void getEmployeeResponseGetsEmployeeResponse()
//	{
//		TRMSDao dao = makeDao();
//		TRMSService service = makeService(dao);
//		// make a temporary employee so we know it exists
//		// then try to get it
//		String name = String.valueOf(RANDOM.nextInt(10000));
//		String username = String.valueOf(RANDOM.nextInt(10000));
//		String password = String.valueOf(RANDOM.nextInt(10000));
//		Employee newEmployeeData = new Employee(name, username, password, null, null, Collections.emptyList());
//		newEmployeeData = dao.getEmployeeDao().add(newEmployeeData);
//		CREATED_EMPLOYEES.add(newEmployeeData.getEmployeeID());
//		int employeeID = newEmployeeData.getEmployeeID();
//
//		EmployeeResponse response = service.getEmployeeResponse(employeeID, employeeID);
//		Assertions.assertEquals(name, response.getName());
//	}
//
//	@Test
//	void getEmployeeResponseDoesntGetUnauthorizedEmployee()
//	{
//		TRMSDao dao = makeDao();
//		TRMSService service = makeService(dao);
//		// make two temporary employees, then have one try to get the other
//		// this should fail
//		String name = String.valueOf(RANDOM.nextInt(10000));
//		String username = String.valueOf(RANDOM.nextInt(10000));
//		String password = String.valueOf(RANDOM.nextInt(10000));
//
//		Employee newEmployeeDataA = new Employee(name, username, password, null, null, Collections.emptyList());
//		newEmployeeDataA = dao.getEmployeeDao().add(newEmployeeDataA);
//		CREATED_EMPLOYEES.add(newEmployeeDataA.getEmployeeID());
//
//		Employee newEmployeeDataB = new Employee(name, username, password, null, null, Collections.emptyList());
//		newEmployeeDataB = dao.getEmployeeDao().add(newEmployeeDataB);
//		CREATED_EMPLOYEES.add(newEmployeeDataB.getEmployeeID());
//
//		int employeeIDA = newEmployeeDataA.getEmployeeID();
//		int employeeIDB = newEmployeeDataB.getEmployeeID();
//
//		EmployeeResponse response = service.getEmployeeResponse(employeeIDA, employeeIDB);
//		Assertions.assertNull(response);
//	}
//
//	private static TRMSDao makeDao()
//	{
//		return new HibernateTRMSDao(
//			new HibernateCrudDao<>(Approval.class, "approvalID", Approval::setApprovalID),
//			new HibernateCrudDao<>(Attachment.class, "attachmentID", Attachment::setAttachmentID),
//			new HibernateCrudDao<>(Department.class, "departmentID", Department::setDepartmentID),
//			new HibernateCrudDao<>(Employee.class, "employeeID", Employee::setEmployeeID),
//			new HibernateCrudDao<>(EventType.class, "eventTypeID", EventType::setEventTypeID),
//			new HibernateCrudDao<>(GradingFormat.class, "formatID", GradingFormat::setFormatID),
//			new HibernateCrudDao<>(ReimbursementRequest.class, "requestID", ReimbursementRequest::setRequestID),
//			new HibernateCrudDao<>(Role.class, "roleID", Role::setRoleID));
//	}
//
//	private static TRMSService makeService(TRMSDao dao)
//	{
//		Properties props = new Properties();
//		try (InputStream input = App.class.getClassLoader().getResourceAsStream("trms.properties"))
//		{
//			props.load(input);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		String hostURL = props.getProperty("hostURL");
//		return new TRMSServiceImpl(dao, hostURL);
//	}
}
