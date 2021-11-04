package com.revature.trms.models;

import com.sun.istack.internal.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="employees", schema="trms")
public class Employee
{
	@Id
	@Column(name="employee_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeID;

	@Column(name="name", nullable = false, length=50)
	private @NotNull String name;

	@Column(name="username", nullable=false, length=50)
	private @NotNull String username;

	@Column(name="password", nullable=false, length=50)
	private @NotNull String password;

	@ManyToOne
	@JoinColumn(name="supervisor_id")
	private Employee supervisor;

	// specifies that this column is a non-unique foreign key
	// (we could also use OneToMany on the other side with a list field)
	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;

	// we implement many-to-many relations by having one of the models have a list of the other
	// (even if the actual tables are implemented by having a junction table)

	// eager vs lazy fetching
	// eager = retrieve joined data immediately
	// lazy = retrieve joined data once referenced (might help avoid dumping wodges of data into your web server's RAM)
	@ManyToMany(fetch=FetchType.EAGER)
	// JoinTable creates a mapping to a junction table
	@JoinTable(name="employee_role_junctions",
		joinColumns = @JoinColumn(name="employee_id", nullable = false),
		inverseJoinColumns = @JoinColumn(name="role_id", nullable = false))
	private List<Role> roles;

	public Employee()
	{
		this("", "", "", null, null, new ArrayList<>());
	}

	public Employee(String name, String username, String password, Employee supervisor, Department department, List<Role> roles)
	{
		this.name = name;
		this.username = username;
		this.password = password;
		this.supervisor = supervisor;
		this.department = department;
		this.roles = roles;
	}

	public int getEmployeeID()
	{
		return this.employeeID;
	}

	public void setEmployeeID(int employeeID)
	{
		this.employeeID = employeeID;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Employee getSupervisor()
	{
		return this.supervisor;
	}

	public void setSupervisor(Employee supervisor)
	{
		this.supervisor = supervisor;
	}

	public Department getDepartment()
	{
		return this.department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public List<Role> getRoles()
	{
		return this.roles;
	}

	public void setRoles(List<Role> roles)
	{
		this.roles = roles;
	}
}
