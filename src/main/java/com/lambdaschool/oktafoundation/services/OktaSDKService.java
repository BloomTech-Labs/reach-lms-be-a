package com.lambdaschool.oktafoundation.services;


import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;


public interface OktaSDKService {

	boolean containsUser(String email);
	UserList getUsers();
	UserList getUsers(String query);
	User createOktaUser(com.lambdaschool.oktafoundation.models.User reachUser);
	User createOktaUser(
			String email,
			String firstname,
			String lastname,
			String role
	);
	GroupList getGroups();
	GroupList getGroups(String query);

}
