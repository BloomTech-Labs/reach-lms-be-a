package com.lambdaschool.oktafoundation.models;


import javax.validation.constraints.NotNull;


public class MinimumUser {

	@NotNull
	private String   email;
	@NotNull
	private RoleType roleType;
	private String   username;
	private String   firstName;
	private String   lastName;

	public MinimumUser() {}

	public MinimumUser(
			String email,
			String name
	) {
		this.roleType = RoleType.STUDENT;
		this.username = email;
		this.email    = email;
		String[] splitName = name.split(" ");
		this.firstName = splitName[0];
		if (splitName.length > 1) {
			this.lastName = splitName[1];
		} else {
			this.lastName = firstName;
		}
	}

	public MinimumUser(
			String username,
			String email,
			String firstName,
			String lastName
	) {
		this.username  = username;
		this.email     = email;
		this.firstName = firstName;
		this.lastName  = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	//	// supporting all lowers for reverse compatibility
	//	public String getFirstname() {
	//		return getFirstName();
	//	}
	//
	//	// supporting all lowers for reverse compatibility
	//	public String getLastname() {
	//		return getLastName();
	//	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

}
