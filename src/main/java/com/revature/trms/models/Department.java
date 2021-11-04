package com.revature.trms.models;

import com.sun.istack.internal.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="departments", schema="trms")
public class Department
{
	// the department_id column in my departments table
	@Id
	@Column(name="department_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int departmentID;

	// the name column in my departments table
	@Column(name="name", nullable = false, length = 50)
	private @NotNull String name;

	// the department_id and department_head_employee_id columns in my department_heads junction table
	@ManyToOne
	@JoinTable(name="department_heads",
		joinColumns = @JoinColumn(name="department_id", nullable = false, unique = true),
		inverseJoinColumns = @JoinColumn(name="department_head_employee_id"))
	private Employee departmentHead;

	public Department()
	{
		this("", null);
	}

	public Department(String name, Employee departmentHead)
	{
		this.name = name;
		this.departmentHead = departmentHead;
	}

	public int getDepartmentID()
	{
		return this.departmentID;
	}

	public void setDepartmentID(int departmentID)
	{
		this.departmentID = departmentID;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Employee getDepartmentHead()
	{
		return this.departmentHead;
	}

	public void setDepartmentHead(Employee departmentHead)
	{
		this.departmentHead = departmentHead;
	}

	@Override
	public String toString()
	{
		return "Department{" +
			"departmentID=" + this.departmentID +
			", name='" + this.name + '\'' +
			", departmentHead=" + this.departmentHead +
			'}';
	}
}
