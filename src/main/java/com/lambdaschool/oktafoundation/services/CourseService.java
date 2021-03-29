package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Course;

import java.util.List;


public interface CourseService {

	List<Course> findAll();

	List<Course> findRelevant();
	List<Course> findRelevant(String query);

	Course findCourseById(long courseId);

	List<Course> findByTag(String tagTitle);

	Course save(
			Course course,
			long programid
	);

	Course save(
			long programId,
			Course course
	);

	Course update(
			long courseId,
			Course course
	);

	void delete(long courseId);


}
