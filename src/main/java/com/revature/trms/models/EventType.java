package com.revature.trms.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

/*
@Entity denotes instances of this class as managed entities that hibernate will manage.
By extension, this class will have a DB table for it.

@Table provides additional information about the table itself.
table name, schema, etc
*/
@Entity
@Table(name="event_types", schema="trms")
public class EventType
{
	@Id // denotes the primary key for this table
	@Column(name="event_type_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eventTypeID;

	@Column(name="name", nullable = false, length=50)
	private @NotNull String name;

	@Column(name="coverage_percentage", nullable = false)
	private int coveragePercentage;

	public EventType()
	{
		this("", 0);
	}

	public EventType(@NotNull String name, int coveragePercentage)
	{
		this.name = name;
		this.coveragePercentage = coveragePercentage;
	}

	public int getEventTypeID()
	{
		return eventTypeID;
	}

	public void setEventTypeID(int eventTypeID)
	{
		this.eventTypeID = eventTypeID;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	public void setName(@NotNull String name)
	{
		this.name = name;
	}

	public int getCoveragePercentage()
	{
		return coveragePercentage;
	}

	public void setCoveragePercentage(int coveragePercentage)
	{
		this.coveragePercentage = coveragePercentage;
	}

	@Override
	public String toString()
	{
		return "EventType" +
			"eventTypeID=" + eventTypeID +
			", name='" + name + '\'' +
			", coveragePercentage=" + coveragePercentage +
			'}';
	}
}
