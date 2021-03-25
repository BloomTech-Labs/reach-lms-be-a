package com.lambdaschool.oktafoundation.models;


import javax.validation.constraints.NotNull;


public class MinimumUser {

	@NotNull
	private String   email;
	@NotNull
	private RoleType roleType;
	private String   username;
	private String   firstname;
	private String   lastname;

	public MinimumUser() {}

	public MinimumUser(
			String email,
			String name
	) {
		this.roleType = RoleType.STUDENT;
		this.username = email;
		this.email    = email;
		String[] splitName = name.split(" ");
		this.firstname = splitName[0];
		if (splitName.length > 1) {
			this.lastname = splitName[1];
		} else {
			this.lastname = firstname;
		}
	}

	public MinimumUser(
			String username,
			String email,
			String firstname,
			String lastname
	) {
		this.username  = username;
		this.email     = email;
		this.firstname = firstname;
		this.lastname  = lastname;
		this.roleType  = RoleType.STUDENT;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

}
