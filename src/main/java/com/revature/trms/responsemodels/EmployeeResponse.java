package com.revature.trms.responsemodels;

public class EmployeeResponse
{
	private final String name;

	public EmployeeResponse(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}
}
