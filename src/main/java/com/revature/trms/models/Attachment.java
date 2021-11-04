package com.revature.trms.models;

import com.revature.trms.util.ApprovalLevel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="request_attachments", schema="trms")
public class Attachment
{
	@Id
	@Column(name="attachment_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int attachmentID;

	@ManyToOne
	@JoinColumn(name="request_id", nullable = true)
	private ReimbursementRequest request;

	@Column(name="description", nullable = false, length=500)
	private String description;

	@Column(name="location", nullable = false, length=500)
	private String location;

	@Column(name="approval_level", nullable = true)
	private ApprovalLevel approvalLevel;

	@Column(name="proof_of_completion", nullable = false)
	private boolean proofOfCompletion;

	public Attachment()
	{

	}

	public Attachment(ReimbursementRequest request, String description, String location, ApprovalLevel approvalLevel, boolean proofOfCompletion)
	{
		this.request = request;
		this.description = description;
		this.location = location;
		this.approvalLevel = approvalLevel;
		this.proofOfCompletion = proofOfCompletion;
	}

	public int getAttachmentID()
	{
		return this.attachmentID;
	}

	public void setAttachmentID(int attachmentID)
	{
		this.attachmentID = attachmentID;
	}

	public ReimbursementRequest getRequest()
	{
		return this.request;
	}

	public void setRequest(ReimbursementRequest request)
	{
		this.request = request;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public ApprovalLevel getApprovalLevel()
	{
		return this.approvalLevel;
	}

	public void setApprovalLevel(ApprovalLevel approvalLevel)
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

	@Override
	public String toString()
	{
		return "Attachment{" +
			"attachmentID=" + this.attachmentID +
			", request=" + this.request +
			", description='" + this.description + '\'' +
			", location='" + this.location + '\'' +
			", approvalLevel=" + this.approvalLevel +
			", proofOfCompletion=" + this.proofOfCompletion +
			'}';
	}
}
