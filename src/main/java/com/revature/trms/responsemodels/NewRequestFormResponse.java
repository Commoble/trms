package com.revature.trms.responsemodels;

import com.revature.trms.models.EventType;
import com.revature.trms.models.GradingFormat;

import java.util.List;

public class NewRequestFormResponse
{
	private int remainingReimbursement;
	private List<EventType> eventTypes;
	private List<GradingFormat> gradingFormats;

	public NewRequestFormResponse(int remainingReimbursement, List<EventType> eventTypes, List<GradingFormat> gradingFormats)
	{
		this.remainingReimbursement = remainingReimbursement;
		this.eventTypes = eventTypes;
		this.gradingFormats = gradingFormats;
	}

	public int getRemainingReimbursement()
	{
		return this.remainingReimbursement;
	}

	public void setRemainingReimbursement(int remainingReimbursement)
	{
		this.remainingReimbursement = remainingReimbursement;
	}

	public List<EventType> getEventTypes()
	{
		return this.eventTypes;
	}

	public void setEventTypes(List<EventType> eventTypes)
	{
		this.eventTypes = eventTypes;
	}

	public List<GradingFormat> getGradingFormats()
	{
		return this.gradingFormats;
	}

	public void setGradingFormats(List<GradingFormat> gradingFormats)
	{
		this.gradingFormats = gradingFormats;
	}
}
