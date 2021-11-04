package com.revature.trms.requestmodels;

public class NewApprovalRequestJson
{
	private int requestID;
	private boolean approved;
	private String comments;

	public NewApprovalRequestJson(int requestID, boolean approved, String comments)
	{
		this.requestID = requestID;
		this.approved = approved;
		this.comments = comments;
	}

	public int getRequestID()
	{
		return this.requestID;
	}

	public void setRequestID(int requestID)
	{
		this.requestID = requestID;
	}

	public boolean isApproved()
	{
		return this.approved;
	}

	public void setApproved(boolean approved)
	{
		this.approved = approved;
	}

	public String getComments()
	{
		return this.comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}
}
