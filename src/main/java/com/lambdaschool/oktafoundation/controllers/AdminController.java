package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.models.MinimumUser;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


//@RestController
public class AdminController {

	@Autowired
	public Client client;

	/**
	 * Return all users in this Okta application
	 *
	 * @return All users associated with this Okta application
	 */
	@GetMapping("/okta/users")
	public UserList getUsers() {
		return client.listUsers();
	}

	/**
	 * Filter users using firstName, lastName, or email as query params
	 *
	 * @param query firstName, lastName, or email to search for
	 *
	 * @return List of users that match the given query.
	 */
	@GetMapping("/okta/user")
	public UserList searchUserByEmail(
			@RequestParam
					String query
	) {
		return client.listUsers(query, null, null, null, null);
	}

	@PostMapping("okta/createUser")
	public User createUser(
			@Valid
			@RequestBody
					MinimumUser newUser
	) {
		char[] tempPassword = {'P', 'a', '$', '$', 'w', '0', 'r', 'd'};
		return UserBuilder.instance()
				.setEmail(newUser.getEmail())
				.setFirstName(newUser.getFirstname())
				.setLastName(newUser.getLastname())
				.setPassword(tempPassword)
				.setActive(true)
				.buildAndCreate(client);
	}

}
