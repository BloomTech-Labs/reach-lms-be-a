package com.lambdaschool.oktafoundation.services;


import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OktaSDKServiceImpl
		implements OktaSDKService {

	@Autowired
	public Client client;

	@Override
	public UserList getUsers() {
		return client.listUsers();
	}

	@Override
	public UserList searchUsersByEmail(String query) {
		return client.listUsers(query, null, null, null, null);
	}

	@Override
	public User createOktaUser(
			String email,
			String firstname,
			String lastname,
			String role
	) {
		// I think we could also add a Group attribute here, but I'm holding off until I'm certain about matching
		// groups with Okta is the correct choice.
		GroupList possibleGroupMatches = client.listGroups(role, null, null);
		Group     groupToAttach        = possibleGroupMatches.single();
		//		System.out.println(groupToAttach);
		return UserBuilder.instance()
				.setLogin(email)
				.setEmail(email)
				.setFirstName(firstname)
				.setLastName(lastname)
				.setActive(false)
				.buildAndCreate(client);
	}

	@Override
	public GroupList getGroups() {
		return client.listGroups();
	}

}
