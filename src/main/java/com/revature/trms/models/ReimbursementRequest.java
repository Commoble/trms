package com.revature.trms.models;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="reimbursement_requests", schema="trms")
public class ReimbursementRequest
{
	@Id
	@Column(name="request_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestID;

	@ManyToOne
	@JoinColumn(name="employee_id", nullable = false)
	private Employee employee;

	@Column(name="awarded", nullable = false)
	private boolean awarded;

	@ManyToOne
	@JoinColumn(name="event_type", nullable = false)
	private EventType eventType;

	@Column(name="event_date")
	private @Nullable Long eventDate; // unix epoch millis, null indicates no date specified

	@Column(name="event_address", nullable=false, length=100)
	private @NotNull String eventAddress;

	@Column(name="event_description", nullable=false, length=500)
	private @NotNull String eventDescription;

	@Column(name="event_cost", nullable=false)
	private int eventCost; // in american cents

	@Column(name="reimbursement_amount", nullable = false)
	private int reimbursementAmount;

	@Column(name="excessive_reimbursement", nullable = false)
	private int excessiveReimbursement;

	@ManyToOne
	@JoinColumn(name="grading_format", nullable=false)
	private GradingFormat gradingFormat;

	@Column(name="passing_grade", nullable=false, length=20)
	private String passingGrade;

	@Column(name="work_related_justification", nullable = false, length=500)
	private @NotNull String workRelatedJustification;

	@Column(name="submission_time", nullable = false)
	private long submissionTime; // unix epoch millis;

	@Column(name="requestor_must_review_modifications", nullable=false)
	private boolean requestorMustReviewModifications;

	@Column(name="work_time_missed", nullable = false, length=500)
	private @NotNull String workTimeMissed;

//	@OneToMany(mappedBy="request") // non-owning side, bidirectional
//	private List<Attachment> attachments;
//
//	@OneToMany(mappedBy="request") // non-owning side, bidirectional
//	private List<Approval> approvals;

	public ReimbursementRequest()
	{
		this(null, false, null, 0L, "", "", 0, 0, 0, null, "", "", 0, false, "");
	}

	public ReimbursementRequest(Employee employee, boolean awarded, EventType eventType, Long eventDate, String eventAddress, String eventDescription, int eventCost, int reimbursementAmount, int excessiveReimbursement, GradingFormat gradingFormat, String passingGrade, String workRelatedJustification, long submissionTime, boolean requestorMustReviewModifications, String workTimeMissed)
	{
		this.employee = employee;
		this.awarded = awarded;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.eventAddress = eventAddress;
		this.eventDescription = eventDescription;
		this.eventCost = eventCost;
		this.reimbursementAmount = reimbursementAmount;
		this.excessiveReimbursement = excessiveReimbursement;
		this.gradingFormat = gradingFormat;
		this.passingGrade = passingGrade;
		this.workRelatedJustification = workRelatedJustification;
		this.submissionTime = submissionTime;
		this.requestorMustReviewModifications = requestorMustReviewModifications;
		this.workTimeMissed = workTimeMissed;
//		this.attachments = attachments;
//		this.approvals = approvals;
	}

	public int getRequestID()
	{
		return this.requestID;
	}

	public void setRequestID(int requestID)
	{
		this.requestID = requestID;
	}

	public Employee getEmployee()
	{
		return this.employee;
	}

	public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}

	public boolean isAwarded()
	{
		return this.awarded;
	}

	public void setAwarded(boolean awarded)
	{
		this.awarded = awarded;
	}

	public EventType getEventType()
	{
		return this.eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public Long getEventDate()
	{
		return this.eventDate;
	}

	public void setEventDate(Long eventDate)
	{
		this.eventDate = eventDate;
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

	public int getEventCost()
	{
		return this.eventCost;
	}

	public void setEventCost(int eventCost)
	{
		this.eventCost = eventCost;
	}

	public int getReimbursementAmount()
	{
		return this.reimbursementAmount;
	}

	public void setReimbursementAmount(int reimbursementAmount)
	{
		this.reimbursementAmount = reimbursementAmount;
	}

	public int getExcessiveReimbursement()
	{
		return this.excessiveReimbursement;
	}

	public void setExcessiveReimbursement(int excessiveReimbursement)
	{
		this.excessiveReimbursement = excessiveReimbursement;
	}

	public GradingFormat getGradingFormat()
	{
		return this.gradingFormat;
	}

	public void setGradingFormat(GradingFormat gradingFormat)
	{
		this.gradingFormat = gradingFormat;
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

	public long getSubmissionTime()
	{
		return this.submissionTime;
	}

	public void setSubmissionTime(long submissionTime)
	{
		this.submissionTime = submissionTime;
	}

	public boolean isRequestorMustReviewModifications()
	{
		return this.requestorMustReviewModifications;
	}

	public void setRequestorMustReviewModifications(boolean requestorMustReviewModifications)
	{
		this.requestorMustReviewModifications = requestorMustReviewModifications;
	}

	public String getWorkTimeMissed()
	{
		return this.workTimeMissed;
	}

	public void setWorkTimeMissed(String workTimeMissed)
	{
		this.workTimeMissed = workTimeMissed;
	}

//	public List<Attachment> getAttachments()
//	{
//		return this.attachments;
//	}
//
//	public void setAttachments(List<Attachment> attachments)
//	{
//		this.attachments = attachments;
//	}
//
//	public List<Approval> getApprovals()
//	{
//		return this.approvals;
//	}
//
//	public void setApprovals(List<Approval> approvals)
//	{
//		this.approvals = approvals;
//	}
}
