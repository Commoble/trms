package com.revature.trms.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name="roles", schema="trms")
public class Role
{
	@Id
	@Column(name="role_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleID;

	@Column(name="name", nullable = false, length=50)
	private @NotNull String name;

	@Column(name="can_coordinate_benefits", nullable=false)
	private boolean canCoordinateBenefits;

	public Role()
	{
		this("", false);
	}

	public Role(@NotNull String name, boolean canCoordinateBenefits)
	{
		this.name = name;
		this.canCoordinateBenefits = canCoordinateBenefits;
	}

	public int getRoleID()
	{
		return roleID;
	}

	public void setRoleID(int roleID)
	{
		this.roleID = roleID;
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

	public boolean canCoordinateBenefits()
	{
		return canCoordinateBenefits;
	}

	public void setCanCoordinateBenefits(boolean canCoordinateBenefits)
	{
		this.canCoordinateBenefits = canCoordinateBenefits;
	}

	@Override
	public String toString()
	{
		return "Role{" +
			"roleID=" + roleID +
			", name='" + name + '\'' +
			", canCoordinateBenefits=" + canCoordinateBenefits +
			'}';
	}
}
