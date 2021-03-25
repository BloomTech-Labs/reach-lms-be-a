package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.models.MinimumUser;
import com.lambdaschool.oktafoundation.services.OktaSDKService;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class OktaController {

	@Autowired
	OktaSDKService okta;

	@Autowired
	Client client;


	/**
	 * Return all users in this Okta application
	 *
	 * @return All users associated with this Okta application
	 */
	@GetMapping("/okta/users")
	public UserList getUsers() {
		return okta.getUsers();
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
		return okta.getUsers(query);
	}

	@PostMapping("/okta/createUser")
	public User createUser(
			@Valid
			@RequestBody
					MinimumUser newUser
	) {
		return okta.createOktaUser(newUser.getEmail(),
				newUser.getFirstname(),
				newUser.getLastname(),
				newUser.getRoleType()
						.name()
		);
	}

	@GetMapping("/okta/groups")
	public GroupList getGroups() {
		return okta.getGroups();
	}

	@GetMapping("/okta/groups_query")
	public GroupList getGroupsQ(@RequestParam String query) {
		return client.listGroups(query, null, null);
	}

}
