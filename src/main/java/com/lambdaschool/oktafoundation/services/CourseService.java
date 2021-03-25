package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Course;

import java.util.List;


public interface CourseService {

	List<Course> findAll();

	Course findCourseById(long courseId);

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
