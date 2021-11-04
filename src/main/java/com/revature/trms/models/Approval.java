package com.revature.trms.models;

import com.revature.trms.util.ApprovalLevel;
import com.sun.istack.internal.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="approvals", schema="trms")
public class Approval
{
	@Id
	@Column(name="approval_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int approvalID;

	@ManyToOne
	@JoinColumn(name="approver_id", nullable = false)
	private Employee approver;

	@ManyToOne
	@JoinColumn(name="request_id", nullable = false)
	private ReimbursementRequest request;

	@Column(name="approval_level", nullable = false)
	private @NotNull ApprovalLevel approvalLevel; // 1=supervisor, 2=department, 3=benco

	@Column(name="approved", nullable = false)
	private boolean approved; // true if approved, false if denied

	@Column(name="time", nullable = false)
	private long time; // time approval was submitted (in unix epoch millis)

	@Column(name="comments", nullable=false, length=500)
	private @NotNull String comments;

	public Approval()
	{
		this(null,null,ApprovalLevel.NONE,false,0,"");
	}

	public Approval(Employee approver, ReimbursementRequest request, ApprovalLevel approvalLevel, boolean approved, long time, String comments)
	{
		this.approver = approver;
		this.request = request;
		this.approvalLevel = approvalLevel;
		this.approved = approved;
		this.time = time;
		this.comments = comments;
	}

	public int getApprovalID()
	{
		return this.approvalID;
	}

	public void setApprovalID(int approvalID)
	{
		this.approvalID = approvalID;
	}

	public Employee getApprover()
	{
		return this.approver;
	}

	public void setApprover(Employee approver)
	{
		this.approver = approver;
	}

	public ReimbursementRequest getRequest()
	{
		return this.request;
	}

	public void setRequest(ReimbursementRequest request)
	{
		this.request = request;
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

	public long getTime()
	{
		return this.time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public String getComments()
	{
		return this.comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	@Override
	public String toString()
	{
		return "Approval{" +
			"approvalID=" + this.approvalID +
			", approver=" + this.approver +
			", request=" + this.request +
			", approvalLevel=" + this.approvalLevel +
			", approved=" + this.approved +
			", time=" + this.time +
			", comments='" + this.comments + '\'' +
			'}';
	}
}
