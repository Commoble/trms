package com.revature.trms.app;

import com.revature.trms.controllers.JavalinController;
import com.revature.trms.controllers.TRMSController;
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
import com.revature.trms.services.TRMSService;
import com.revature.trms.services.TRMSServiceImpl;
import com.revature.trms.util.HibernateUtils;
import io.javalin.Javalin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App
{
	public static void main(String[] args)
	{
		HibernateUtils.bootstrap();
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

		TRMSDao dao = new HibernateTRMSDao(
			new HibernateCrudDao<>(Approval.class, "approvalID", Approval::setApprovalID),
			new HibernateCrudDao<>(Attachment.class, "attachmentID", Attachment::setAttachmentID),
			new HibernateCrudDao<>(Department.class, "departmentID", Department::setDepartmentID),
			new HibernateCrudDao<>(Employee.class, "employeeID", Employee::setEmployeeID),
			new HibernateCrudDao<>(EventType.class, "eventTypeID", EventType::setEventTypeID),
			new HibernateCrudDao<>(GradingFormat.class, "formatID", GradingFormat::setFormatID),
			new HibernateCrudDao<>(ReimbursementRequest.class, "requestID", ReimbursementRequest::setRequestID),
			new HibernateCrudDao<>(Role.class, "roleID", Role::setRoleID));
		TRMSService service = new TRMSServiceImpl(dao, hostURL);
		JavalinController controller = new TRMSController(service);
		Javalin javalin = Javalin.create(controller::configureJavalin);
		controller.registerEndpoints(javalin);
		javalin.start();
	}
}
