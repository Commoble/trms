package com.revature.trms.requestmodels;

import com.revature.trms.util.ApprovalLevel;

public class NewAttachmentRequestJson
{
	private int requestID;
	private String description;
	private ApprovalLevel approvalLevel;
	private boolean proofOfCompletion;

	public NewAttachmentRequestJson()
	{

	}

	public int getRequestID()
	{
		return this.requestID;
	}

	public void setRequestID(int requestID)
	{
		this.requestID = requestID;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ApprovalLevel getApprovalLevel()
	{
		return this.approvalLevel;
	}

	public void ApprovalLevel(ApprovalLevel approvalLevel)
	{
		this.approvalLevel = approvalLevel;
	}

	public boolean isProofOfCompletion()
	{
		return this.proofOfCompletion;
	}

	public void setProofOfCompletion(boolean proofOfCompletion)
	{
		this.proofOfCompletion = proofOfCompletion;
	}
}
