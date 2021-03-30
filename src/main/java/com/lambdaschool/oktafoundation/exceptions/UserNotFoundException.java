package com.lambdaschool.oktafoundation.exceptions;


import com.lambdaschool.oktafoundation.models.Course;


public class UserNotFoundException
		extends ResourceNotFoundException {

	public UserNotFoundException(long id) {
		super("Could not find user with id " + id);
	}

	public UserNotFoundException(String name) {
		super("Could not find user with name " + name);
	}

	public UserNotFoundException(long id, Course course) {
		super("User with id " + id + " is not part of course with id " + course.getCourseid());
	}

	public UserNotFoundException(String name, Course course) {
		super("User with name " + name + " is not part of the course with name " + course.getCoursename());
	}

}
