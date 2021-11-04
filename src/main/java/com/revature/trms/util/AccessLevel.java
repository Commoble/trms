package com.revature.trms.util;

import io.javalin.core.security.Role;

import java.util.Set;

/**
 * Access levels for accessing endpoints
 */
public enum AccessLevel implements Role
{
	/** "anyone on the internet" **/
	ANYONE,
	/** An employee with valid auth credentials**/
	EMPLOYEE,
	/** An employee with auth credentials *and* full database access **/
	ADMIN;

	public static final Set<Role> ANYONE_PLUS = CollectionUtils.setOf(ANYONE,EMPLOYEE,ADMIN);
	public static final Set<Role> EMPLOYEE_PLUS = CollectionUtils.setOf(EMPLOYEE,ADMIN);
	public static final Set<Role> ADMIN_PLUS = CollectionUtils.setOf(ADMIN);
}
