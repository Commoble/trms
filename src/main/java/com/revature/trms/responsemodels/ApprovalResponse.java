package com.revature.trms.responsemodels;

import com.revature.trms.util.ApprovalLevel;

public class ApprovalResponse
{
	private String approver;
	private ApprovalLevel approvalLevel;
	private boolean approved;
	private long approvalDate;
	private String comments;

	public ApprovalResponse(String approver, ApprovalLevel approvalLevel, boolean approved, long approvalDate, String comments)
	{
		this.approver = approver;
		this.approvalLevel = approvalLevel;
		this.approved = approved;
		this.approvalDate = approvalDate;
		this.comments = comments;
	}

	public String getApprover()
	{
		return this.approver;
	}

	public void setApprover(String approver)
	{
		this.approver = approver;
	}

	public ApprovalLevel getApprovalLevel()
	{
		return this.approvalLevel;
	}

	public void setApprovalLevel(ApprovalLevel approvalLevel)
	{
		this.approvalLevel = approvalLevel;
	}

	public boolean isApproved()
	{
		return this.approved;
	}

	public void setApproved(boolean approved)
	{
		this.approved = approved;
	}

	public long getApprovalDate()
	{
		return this.approvalDate;
	}

	public void setApprovalDate(long approvalDate)
	{
		this.approvalDate = approvalDate;
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
