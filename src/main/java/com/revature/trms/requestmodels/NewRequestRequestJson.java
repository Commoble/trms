package com.revature.trms.requestmodels;

public class NewRequestRequestJson
{
	private int eventType;
	private String eventDate;
	private String eventAddress;
	private String eventDescription;
	private String eventCost;
	private int gradingFormat;
	private String workRelatedJustification;
	private String passingGrade;
	private String workTimeMissed;

	public NewRequestRequestJson()
	{

	}

	public NewRequestRequestJson(int eventType, String eventDate, String eventAddress, String eventDescription, String eventCost, int gradingFormat, String workRelatedJustification, String passingGrade, String workTimeMissed)
	{
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.eventAddress = eventAddress;
		this.eventDescription = eventDescription;
		this.eventCost = eventCost;
		this.gradingFormat = gradingFormat;
		this.workRelatedJustification = workRelatedJustification;
		this.passingGrade = passingGrade;
		this.workTimeMissed = workTimeMissed;
	}

	public int getEventType()
	{
		return this.eventType;
	}

	public void setEventType(int eventType)
	{
		this.eventType = eventType;
	}

	public String getEventDate()
	{
		return this.eventDate;
	}

	public void setEventDate(String eventDate)
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

	public String getEventCost()
	{
		return this.eventCost;
	}

	public void setEventCost(String eventCost)
	{
		this.eventCost = eventCost;
	}

	public int getGradingFormat()
	{
		return this.gradingFormat;
	}

	public void setGradingFormat(int gradingFormat)
	{
		this.gradingFormat = gradingFormat;
	}

	public String getWorkRelatedJustification()
	{
		return this.workRelatedJustification;
	}

	public void setWorkRelatedJustification(String workRelatedJustification)
	{
		this.workRelatedJustification = workRelatedJustification;
	}

	public String getPassingGrade()
	{
		return this.passingGrade;
	}

	public void setPassingGrade(String passingGrade)
	{
		this.passingGrade = passingGrade;
	}

	public String getWorkTimeMissed()
	{
		return this.workTimeMissed;
	}

	public void setWorkTimeMissed(String workTimeMissed)
	{
		this.workTimeMissed = workTimeMissed;
	}
}
