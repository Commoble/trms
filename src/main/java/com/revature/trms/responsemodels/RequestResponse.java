package com.revature.trms.responsemodels;

import java.util.List;

public class RequestResponse
{
	private int requestID;
	private int employeeID;
	private String employeeName;
	private String eventTime;
	private String submissionTime;
	private String status;
	private int eventTypeID;
	private String eventTypeName;
	private String eventAddress;
	private String eventDescription;
	private String eventCost;
	private String reimbursementAmount;
	private int gradingFormatID;
	private String gradingFormatName;
	private String passingGrade;
	private String workRelatedJustification;
	private String workTimeMissed;
	private boolean requestorMustReviewModifications;
	private List<ApprovalResponse> approvals;

	public RequestResponse(int requestID, int employeeID, String employeeName, String eventTime, String submissionTime, String status, int eventTypeID, String eventTypeName, String eventAddress, String eventDescription, String eventCost, String reimbursementAmount, int gradingFormatID, String gradingFormatName, String passingGrade, String workRelatedJustification, String workTimeMissed, boolean requestorMustReviewModifications, List<ApprovalResponse> approvals)
	{
		this.requestID = requestID;
		this.employeeID = employeeID;
		this.employeeName = employeeName;
		this.eventTime = eventTime;
		this.submissionTime = submissionTime;
		this.status = status;
		this.eventTypeID = eventTypeID;
		this.eventTypeName = eventTypeName;
		this.eventAddress = eventAddress;
		this.eventDescription = eventDescription;
		this.eventCost = eventCost;
		this.reimbursementAmount = reimbursementAmount;
		this.gradingFormatID = gradingFormatID;
		this.gradingFormatName = gradingFormatName;
		this.passingGrade = passingGrade;
		this.workRelatedJustification = workRelatedJustification;
		this.workTimeMissed = workTimeMissed;
		this.requestorMustReviewModifications = requestorMustReviewModifications;
		this.approvals = approvals;
	}

	public int getRequestID()
	{
		return this.requestID;
	}

	public int getEmployeeID()
	{
		return this.employeeID;
	}

	public void setEmployeeID(int employeeID)
	{
		this.employeeID = employeeID;
	}

	public String getEmployeeName()
	{
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getEventTime()
	{
		return this.eventTime;
	}

	public void setEventTime(String eventTime)
	{
		this.eventTime = eventTime;
	}

	public String getSubmissionTime()
	{
		return this.submissionTime;
	}

	public void setSubmissionTime(String submissionTime)
	{
		this.submissionTime = submissionTime;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getEventTypeID()
	{
		return this.eventTypeID;
	}

	public void setEventTypeID(int eventTypeID)
	{
		this.eventTypeID = eventTypeID;
	}

	public String getEventTypeName()
	{
		return this.eventTypeName;
	}

	public void setEventTypeName(String eventTypeName)
	{
		this.eventTypeName = eventTypeName;
	}

	public String getEventAddress()
	{
		return this.eventAddress;
	}

	public void setEventAddress(String eventAddress)
	{
		this.eventAddress = eventAddress;
	}

	public String getEventDescription()
	{
		return this.eventDescription;
	}

	public void setEventDescription(String eventDescription)
	{
		this.eventDescription = eventDescription;
	}

	public String getEventCost()
	{
		return this.eventCost;
	}

	public void setEventCost(String eventCost)
	{
		this.eventCost = eventCost;
	}

	public String getReimbursementAmount()
	{
		return this.reimbursementAmount;
	}

	public void setReimbursementAmount(String reimbursementAmount)
	{
		this.reimbursementAmount = reimbursementAmount;
	}

	public int getGradingFormatID()
	{
		return this.gradingFormatID;
	}

	public void setGradingFormatID(int gradingFormatID)
	{
		this.gradingFormatID = gradingFormatID;
	}

	public String getGradingFormatName()
	{
		return this.gradingFormatName;
	}

	public void setGradingFormatName(String gradingFormatName)
	{
		this.gradingFormatName = gradingFormatName;
	}

	public String getPassingGrade()
	{
		return this.passingGrade;
	}

	public void setPassingGrade(String passingGrade)
	{
		this.passingGrade = passingGrade;
	}

	public String getWorkRelatedJustification()
	{
		return this.workRelatedJustification;
	}

	public void setWorkRelatedJustification(String workRelatedJustification)
	{
		this.workRelatedJustification = workRelatedJustification;
	}

	public String getWorkTimeMissed()
	{
		return this.workTimeMissed;
	}

	public void setWorkTimeMissed(String workTimeMissed)
	{
		this.workTimeMissed = workTimeMissed;
	}

	public boolean isRequestorMustReviewModifications()
	{
		return this.requestorMustReviewModifications;
	}

	public void setRequestorMustReviewModifications(boolean requestorMustReviewModifications)
	{
		this.requestorMustReviewModifications = requestorMustReviewModifications;
	}

	public void setRequestID(int requestID)
	{
		this.requestID = requestID;
	}

	public List<ApprovalResponse> getApprovals()
	{
		return this.approvals;
	}

	public void setApprovals(List<ApprovalResponse> approvals)
	{
		this.approvals = approvals;
	}
}
