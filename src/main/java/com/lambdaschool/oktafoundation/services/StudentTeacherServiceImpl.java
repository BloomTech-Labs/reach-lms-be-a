package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.RoleNotSufficientException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserCourses;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service(value = "studentTeacherService")
public class StudentTeacherServiceImpl
		implements StudentTeacherService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	HelperFunctions helperFunctions;

	@Override
	public List<User> getAllAdmins() {
		List<User> admins = new ArrayList<>();
		userRepository.findAll()
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.ADMIN) {
						admins.add(user);
					}
				});
		return admins;
	}

	@Override
	public List<User> getAllStudents() {
		List<User> students = new ArrayList<>();

		userRepository.findAll()
				.iterator()
				.forEachRemaining(student -> {
					if (student.getRole() == RoleType.STUDENT) {
						students.add(student);
					}
				});

		return students;
	}

	@Override
	public List<User> getAllTeachers() {
		List<User> teachers = new ArrayList<>();
		userRepository.findAll()
				.iterator()
				.forEachRemaining(teacher -> {
					if (teacher.getRole() == RoleType.TEACHER) {
						teachers.add(teacher);
					}
				});
		return teachers;
	}

	@Override
	public List<User> getCourseAttachedUsers(Long courseid) {
		List<User> users = new ArrayList<>();
		userRepository.findEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(users::add);
		return users;
	}

	@Override
	public List<User> getCourseNotAttachedUsers(Long courseid) {
		List<User> users = new ArrayList<>();
		// find any users who are not in Course.users -- this will initially include ADMIN users
		userRepository.findNotEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(user -> {
					// we only want users to appear here if they are NOT ADMIN users
					if (user.getRole() == RoleType.TEACHER || user.getRole() == RoleType.STUDENT) {
						users.add(user);
					}
				});
		return users;
	}

	@Override
	public void attachUserToCourse(
			Long userid,
			Long courseid
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole == RoleType.STUDENT) {
			throw new RoleNotSufficientException("Students are not allowed to attach users to courses");
		}
		User   currUser   = userService.findUserById(userid);
		Course currCourse = courseService.findCourseById(courseid);
		if (currUser.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course level");
		} else {
			UserCourses newRelationship = new UserCourses(currUser, currCourse);
			currCourse.getUsers()
					.add(newRelationship);
			courseService.update(currCourse.getCourseid(), currCourse);
		}
	}

	@Override
	public void detachUserFromCourse(
			Long userid,
			Long courseid
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole == RoleType.STUDENT) {
			throw new RoleNotSufficientException("Students are not allowed to detach users to courses");
		}
		User   currUser   = userService.findUserById(userid);
		Course currCourse = courseService.findCourseById(courseid);
		if (currUser.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course level");
		} else {
			boolean removed = currCourse.getUsers()
					.removeIf(userCourses -> userCourses.equals(new UserCourses(currUser, currCourse)));
			if (!removed) {
				throw new ResourceNotFoundException("The user to detach is not part of the course.");
			}
			courseService.update(currCourse.getCourseid(), currCourse);

		}
	}

	@Override
	public List<User> getCourseAttachedStudents(Long courseid) {
		List<User> students = new ArrayList<>();
		userRepository.findEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.STUDENT) {
						students.add(user);
					}
				});
		return students;
	}

	@Override
	public List<User> getCourseAttachedTeachers(Long courseid) {
		List<User> teachers = new ArrayList<>();
		userRepository.findEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.TEACHER) {
						teachers.add(user);
					}
				});
		return teachers;
	}

	@Override
	public List<User> getCourseDetachedStudents(Long courseid) {
		List<User> students = new ArrayList<>();
		userRepository.findNotEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.STUDENT) {
						students.add(user);
					}
				});
		return students;
	}

	@Override
	public List<User> getCourseDetachedTeachers(Long courseid) {
		List<User> detachedTeachers = new ArrayList<>();
		userRepository.findNotEnrolledUsers(courseid)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.TEACHER) {
						detachedTeachers.add(user);
					}
				});
		return detachedTeachers;
	}


}
