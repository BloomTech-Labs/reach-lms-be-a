package com.lambdaschool.oktafoundation.exceptions;


public class CourseNotFoundException
		extends ResourceNotFoundException {

	public CourseNotFoundException(String courseName) {
		super("Course with name " + courseName + " could not be found.");
	}

	public CourseNotFoundException(long courseId) {
		super("Course with id " + courseId + " could not be found");
	}

}
