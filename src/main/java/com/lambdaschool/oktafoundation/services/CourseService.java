package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Course;

import java.util.List;


public interface CourseService {

	List<Course> findAll();

	List<Course> findRelevant(String query);

	List<Course> findByUser(long userid);

	Course findCourseById(long courseId);

	Course get(long courseid);
	Course get(String coursename);

	List<Course> findByTag(String tagTitle);

	Course save(Course course);
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
