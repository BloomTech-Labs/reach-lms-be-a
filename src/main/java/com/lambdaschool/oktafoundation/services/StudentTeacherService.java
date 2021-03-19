package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.User;

import java.util.List;


public interface StudentTeacherService {

	// GET all students
	public List<User> getAllStudents();

	// GET all teachers
	public List<User> getAllTeachers();

	// GET all attached users
	public List<User> getCourseAttachedUsers(Long courseid);

	// GET all un-attached (available) users
	public List<User> getCourseNotAttachedUsers(Long courseid);

	// this user is a student? add as a student
	// this user is a teacher? add as a teacher
	public void attachUserToCourse(
			Long userid,
			Long courseid
	);

	// DELETE user from course (we decide role)
	public void detachUserFromCourse(
			Long userid,
			Long courseid
	);

	// POSSIBLE METHODS WE COULD ADD BELOW
	// GET teachers by courseid
	// GET students by courseid
	// GET all un-enrolled students by course id
	// GET all un-enrolled teachers by course id
	// PUT student to course by studentid, courseid
	// PUT teacher to course by teacherid, courseid
	// DELETE student from course by studentid, courseid
	// DELETE teacher from course by teacherid, courseid
	// ??? PUT attach users to course by List<User>
	// ??? move teacher from course to different course


}
