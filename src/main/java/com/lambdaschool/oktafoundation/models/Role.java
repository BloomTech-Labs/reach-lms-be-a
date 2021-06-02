package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


/**
 * The entity allowing interaction with the roles table.
 */
@Entity
@Table(name = "roles")
public class Role
		extends Auditable {

	/**
	 * The primary key (long) of the roles table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long roleId;

	/**
	 * The name (String) of the role. Cannot be null and must be unique.
	 */
	@NotNull
	@Column(unique = true)
	private String name;

	private RoleType roleType;


	/**
	 * Part of the join relationship between user and role
	 * connects roles to the user role combination
	 */
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = "role", allowSetters = true)
	private Set<UserRoles> users = new HashSet<>();

	/**
	 * Default Constructor used primarily by the JPA.
	 */
	public Role() { }

	/**
	 * Given the name, create a new role object. User gets added later
	 *
	 * @param name the name of the role in uppercase
	 */
	public Role(String name) {
		this.name     = name.toUpperCase();
		this.roleType = null;
	}

	public Role(
			String name,
			RoleType roleType
	) {
		this.name     = name;
		this.roleType = roleType;
	}

	/**
	 * Getter for role id
	 *
	 * @return the role id, primary key, (long) of this role
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * Setter for role id, used for seeding data
	 *
	 * @param roleId the new role id, primary key, (long) for this role
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * Getter for role name
	 *
	 * @return role name (String) in uppercase
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for role name
	 *
	 * @param name the new role name (String) for this role, in uppercase
	 */
	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	/**
	 * Getter for user role combinations
	 *
	 * @return A list of user role combinations associated with this role
	 */
	public Set<UserRoles> getUsers() {
		return users;
	}

	/**
	 * Setter for user role combinations
	 *
	 * @param users Change the list of user role combinations associated with this role to this one
	 */
	public void setUsers(Set<UserRoles> users) {
		this.users = users;
	}

	@JsonIgnore
	public RoleType getRoleType() {
		return this.roleType;
	}

}
