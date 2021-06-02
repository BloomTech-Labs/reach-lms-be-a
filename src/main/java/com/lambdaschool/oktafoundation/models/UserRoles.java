package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * The entity allowing interaction with the userRoles table.
 * The join table between users and roles.
 * <p>
 * Table enforces a unique constraint of the combination of userid and roleId.
 * These two together form the primary key.
 * <p>
 * When you have a compound primary key, you must implement Serializable for Hibernate
 * When you implement Serializable you must implement equals and hash code
 */
@Entity
@Table(name = "userRoles")
@IdClass(UserRolesId.class)
public class UserRoles
		extends Auditable
		implements Serializable {

	/**
	 * 1/2 of the primary key (long) for userRoles.
	 * Also is a foreign key into the users table
	 */
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties(value = "roles", allowSetters = true)
	private User user;

	/**
	 * 1/2 of the primary key (long) for userRoles.
	 * Also is a foreign key into the roles table
	 */
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	@JoinColumn(name = "roleId")
	@JsonIgnoreProperties(value = "users", allowSetters = true)
	private Role role;

	/**
	 * Default constructor used primarily by the JPA.
	 */
	public UserRoles() {
	}

	/**
	 * Given the params, create a new user role combination object
	 *
	 * @param user The user object of this relationship
	 * @param role The role object of this relationship
	 */
	public UserRoles(
			User user,
			Role role
	) {
		this.user = user;
		this.role = role;
	}

	/**
	 * The getter for User
	 *
	 * @return the complete user object associated with user role combination
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for user
	 *
	 * @param user change the user object associated with this user role combination to this one.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Getter for role
	 *
	 * @return the complete role object associated with this user role combination
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Setter for role
	 *
	 * @param role change role object associated with this user role combination to this one.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUser(), getRole());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserRoles)) {
			return false;
		}
		UserRoles that = (UserRoles) o;
		return ((user == null) ? 0 : user.getUserId()) == ((that.user == null) ? 0 : that.user.getUserId()) &&
		       ((role == null) ? 0 : role.getRoleId()) == ((that.role == null) ? 0 : that.role.getRoleId());
	}

}
