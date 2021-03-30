package com.lambdaschool.oktafoundation.services;


import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class OktaSDKServiceImpl
		implements OktaSDKService {

	@Autowired
	public Client client;

	@Override
	public boolean containsUser(String email) {
		Optional<User> user = getUsers(email).stream()
				.findFirst();
		return user.isPresent();
	}

	@Override
	public UserList getUsers() {
		return client.listUsers();
	}

	@Override
	public UserList getUsers(String query) {
		return client.listUsers(query, null, null, null, null);
	}

	@Override
	public User createOktaUser(
			String email,
			String firstname,
			String lastname,
			String role
	) {
		GroupList possibleGroupMatches = client.listGroups(role, null, null);
		Group     groupToAttach        = possibleGroupMatches.single();
		String    groupId              = groupToAttach.getId();
		String groupName = groupToAttach.getProfile()
				.getName();
		User stagedUser = UserBuilder.instance()
				.setLogin(email) // make login match email
				.setEmail(email)
				.setFirstName(firstname)
				.setLastName(lastname)
				.addGroup(groupId)
				.setActive(false) // initially create a non-activated user
				.buildAndCreate(client);

		// now that our user exists but isn't yet active,
		// this will send an email to the user
		stagedUser.activate(true);
		return stagedUser;
	}

	@Override
	public GroupList getGroups() {
		return client.listGroups();
	}

	@Override
	public GroupList getGroups(String q) {
		return client.listGroups(q, null, null);
	}

	@Override
	public User createOktaUser(com.lambdaschool.oktafoundation.models.User reachUser) {
		return createOktaUser(reachUser.getEmail(),
				reachUser.getFirstname(),
				reachUser.getLastname(),
				reachUser.getRole()
						.name()
		);
	}

}
