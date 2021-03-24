package com.lambdaschool.oktafoundation.exceptions;


public class UserNotFoundException
		extends ResourceNotFoundException {

	public UserNotFoundException(long id) {
		super("Could not find user with id " + id);
	}

	public UserNotFoundException(String name) {
		super("Could not find user with name " + name);
	}

}
