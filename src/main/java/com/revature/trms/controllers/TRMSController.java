package com.revature.trms.controllers;

import com.google.gson.Gson;
import com.revature.trms.models.Employee;
import com.revature.trms.requestmodels.NewApprovalRequestJson;
import com.revature.trms.requestmodels.NewAttachmentRequestJson;
import com.revature.trms.requestmodels.NewRequestRequestJson;
import com.revature.trms.requestmodels.PatchRequestRequestJson;
import com.revature.trms.responsemodels.ApprovalResponse;
import com.revature.trms.responsemodels.AttachmentResponse;
import com.revature.trms.responsemodels.EmployeeResponse;
import com.revature.trms.responsemodels.NewRequestFormResponse;
import com.revature.trms.responsemodels.RequestResponse;
import com.revature.trms.services.TRMSService;
import com.revature.trms.util.ParseUtils;
import com.revature.trms.util.ServiceResult;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.core.security.BasicAuthCredentials;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

public class TRMSController implements JavalinController
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new Gson();

	private final TRMSService service;

	public TRMSController(TRMSService service)
	{
		this.service = service;
	}

	public void configureJavalin(JavalinConfig config)
	{
		config.enableCorsForAllOrigins();
	}

	public void registerEndpoints(Javalin javalin)
	{
		javalin.get("/employees/:id", this.handleWithLoggedInUser((context, loginID) ->{
//			String idParam = context.pathParam("id");
			// we might want a get api for employee data for admins
			// but it's not necessary for the web app right now
//			// if id param is "login", use the logged in employee,
//			// otherwise, parse it as an id and get that
//			int employeeID = Objects.equals(idParam, "login")
//				? loginID
//				: ParseUtils.safeParseInt(idParam, 0);
			int employeeID = loginID;
			LOGGER.debug("Received request to get employee with id {}", employeeID);
			@Nullable EmployeeResponse response = this.service.getEmployeeResponse(loginID, employeeID);
			if (response == null)
			{
				LOGGER.debug("Employee {} not found, responding with 404", employeeID);
				context.status(HttpStatus.NOT_FOUND_404);
			}
			else
			{
				LOGGER.debug("Responding with data for employee {}", employeeID);
				context.result(GSON.toJson(response));
			}
		}));

		javalin.post("/requests", this.handleWithLoggedInUser((context, loginID)->{
			LOGGER.debug("Received request to post new reimbursement request");
			String body = context.body();
			@Nullable NewRequestRequestJson requestRequest = ParseUtils.parseRequestRequest(GSON, body);
			if (requestRequest == null)
			{
				LOGGER.debug("Request to post new reimbursement request was ill-formatted, responding with 400");
				context.status(HttpStatus.BAD_REQUEST_400);
				return;
			}
			ServiceResult<RequestResponse> result = this.service.addRequest(loginID, requestRequest);
			@Nullable RequestResponse response = result.getValue();
			int status = result.getStatus();
			if (response == null)
			{
				LOGGER.debug("Unable to create new reimbursement request, responding with {}", status);
				context.status(status);
			}
			else
			{
				LOGGER.debug("New reimbursement request was posted successfully, responding with {}} and new data", status);
				context.result(GSON.toJson(response));
				context.status(status);
			}
		}));

		javalin.get("/requests", this.handleWithLoggedInUser((context, loginID)->{
			// B) type, can be of {"own", "approvable"}, defaulting to "own"
			// only used if employee is not null
			@Nullable String typeParam = context.queryParam("type");
			LOGGER.debug("Received request for reimbursement requests where type={}", typeParam);
			List<RequestResponse> requests = Objects.equals(typeParam, "approvable")
				? this.service.getRequestsApprovableBy(loginID)
				: this.service.getRequestsOwnedBy(loginID);
			LOGGER.debug("Responding with {} reimbursement requests", requests.size());
			context.result(GSON.toJson(requests));
		}));

		// get information needed to post a new request
		javalin.get("/requests/new", this.handleWithLoggedInUser((context,loginID)->{
			LOGGER.debug("Received request for new request form info");
			NewRequestFormResponse response = this.service.getNewRequestFormResponse(loginID);
			LOGGER.debug("Responding with new request form info");
			context.result(GSON.toJson(response));
		}));

		javalin.patch("/requests/:id", this.handleWithLoggedInUser((context, loginID)->{
			String idParam = context.pathParam("id");
			int requestID = ParseUtils.safeParseInt(idParam, 0);
			LOGGER.debug("Received request to patch request {}", requestID);
			@Nullable PatchRequestRequestJson body = ParseUtils.safeParseJson(GSON, context.body(), PatchRequestRequestJson.class);
			if (body == null)
			{
				LOGGER.debug("Request to patch reimbursement request was ill-formatted, responding with 400");
				context.status(HttpStatus.BAD_REQUEST_400);
				return;
			}
			if (body.isAward())
			{
				int status = this.service.awardRequest(loginID, requestID);
				LOGGER.debug("Responding to reimbursement request patch request with status {}", status);
				context.status(status);
			}
			else
			{
				LOGGER.debug("Request to patch reimbursement request was ill-formatted, responding with 400");
				context.status(HttpStatus.BAD_REQUEST_400);
			}

		}));

		javalin.delete("/requests/:id", this.handleWithLoggedInUser((context, loginID)->{
			String idParam = context.pathParam("id");
			int requestId = ParseUtils.safeParseInt(idParam, 0);
			LOGGER.debug("Received request to delete reimbursement request {}", requestId);
			boolean deleted = this.service.deleteRequest(loginID, requestId);
			LOGGER.debug(deleted
				? "Deleted request {}, responding with 204"
				: "Unable to delete request {}, responding with 404",
				requestId
			);
			context.status(deleted ? HttpStatus.NO_CONTENT_204 : HttpStatus.NOT_FOUND_404);
		}));

		javalin.post("/approvals", this.handleWithLoggedInUser((context, loginID)->{
			LOGGER.debug("Received request to post new approval");
			String body = context.body();
			@Nullable NewApprovalRequestJson approvalRequest = ParseUtils.parseApprovalRequest(GSON, body);
			if (approvalRequest == null)
			{
				LOGGER.debug("Unable to post new approval due to ill-formatted request body, responding with 400");
				context.status(HttpStatus.BAD_REQUEST_400);
				return;
			}
			ServiceResult<ApprovalResponse> serviceResult = this.service.addApproval(loginID, approvalRequest);
			int status = serviceResult.getStatus();
			@Nullable ApprovalResponse approvalResponse = serviceResult.getValue();
			LOGGER.debug("Responsing to new approval post request with status {}", status);
			if (approvalResponse != null)
			{
				context.result(GSON.toJson(approvalResponse));
			}
			context.status(status);
		}));

//		javalin.post("/attachments", this.handleWithLoggedInUser((context, loginID)->{
//			LOGGER.debug("Received request to post new attachment");
//			String body = context.body();
//			List<UploadedFile> files = context.uploadedFiles();
//			@Nullable NewAttachmentRequestJson attachmentRequest = ParseUtils.parseAttachmentRequest(GSON, body);
//			if (attachmentRequest == null)
//			{
//				LOGGER.debug("Could not post new attachment due to ill-formed request, responding with 400");
//				context.status(HttpStatus.BAD_REQUEST_400);
//				return;
//			}
//			ServiceResult<AttachmentResponse> serviceResult = this.service.addAttachment(loginID, attachmentRequest);
//			@Nullable AttachmentResponse attachmentResponse = serviceResult.getValue();
//			int status = serviceResult.getStatus();
//			LOGGER.debug("Responding to new attachment post request with status {}", status);
//			if (attachmentResponse != null)
//			{
//				context.result(GSON.toJson(attachmentResponse));
//			}
//			context.status(status);
//		}));
	}

	private Handler handleWithLoggedInUser(ObjIntConsumer<Context> doubleHandler)
	{
		return context->
		{
			LOGGER.debug("Authenticating request");
			if (!context.basicAuthCredentialsExist())
			{
				LOGGER.debug("Could not authenticate request, no auth credentials provided");
				context.status(HttpStatus.NOT_FOUND_404);
				return;
			}
			BasicAuthCredentials creds = context.basicAuthCredentials();
			String username = creds.getUsername();
			String password = creds.getPassword();
			@Nullable Employee employee = this.service.getEmployeeByLogin(username, password);
			if (employee == null)
			{
				LOGGER.debug("Could not authenticate request, invalid auth credentials provided");
				context.status(HttpStatus.NOT_FOUND_404);
			}
			else
			{
				LOGGER.debug("Authenticated request");
				doubleHandler.accept(context,employee.getEmployeeID());
			}
		};
	}
}
