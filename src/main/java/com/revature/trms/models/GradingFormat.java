package com.revature.trms.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="grading_formats", schema="trms")
public class GradingFormat
{
	@Id
	@Column(name="format_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int formatID;

	@Column(name="name", nullable=false, length=50)
	private @NotNull String name;

	@Column(name="default_passing_grade", nullable = false, length=20)
	private @NotNull String defaultPassingGrade;

	@Column(name="maximum_grade", nullable=false, length=20)
	private @NotNull String maximumGrade;

	@Column(name="requires_presentation", nullable = false)
	private boolean requiresPresentation;

	public GradingFormat()
	{
		this("", "", "", false);
	}

	public GradingFormat(@NotNull String name, @NotNull String defaultPassingGrade, @NotNull String maximumGrade, boolean requiresPresentation)
	{
		this.name = name;
		this.defaultPassingGrade = defaultPassingGrade;
		this.maximumGrade = maximumGrade;
		this.requiresPresentation = requiresPresentation;
	}

	public int getFormatID()
	{
		return this.formatID;
	}

	public void setFormatID(int formatID)
	{
		this.formatID = formatID;
	}

	@NotNull
	public String getName()
	{
		return this.name;
	}

	public void setName(@NotNull String name)
	{
		this.name = name;
	}

	@NotNull
	public String getDefaultPassingGrade()
	{
		return this.defaultPassingGrade;
	}

	public void setDefaultPassingGrade(@NotNull String defaultPassingGrade)
	{
		this.defaultPassingGrade = defaultPassingGrade;
	}

	@NotNull
	public String getMaximumGrade()
	{
		return this.maximumGrade;
	}

	public void setMaximumGrade(@NotNull String maximumGrade)
	{
		this.maximumGrade = maximumGrade;
	}

	public boolean isRequiresPresentation()
	{
		return this.requiresPresentation;
	}

	public void setRequiresPresentation(boolean requiresPresentation)
	{
		this.requiresPresentation = requiresPresentation;
	}

	@Override
	public String toString()
	{
		return "GradingFormat{" +
			"formatID=" + this.formatID +
			", name='" + this.name + '\'' +
			", defaultPassingGrade='" + this.defaultPassingGrade + '\'' +
			", maximumGrade='" + this.maximumGrade + '\'' +
			", requiresPresentation=" + this.requiresPresentation +
			'}';
	}
}
