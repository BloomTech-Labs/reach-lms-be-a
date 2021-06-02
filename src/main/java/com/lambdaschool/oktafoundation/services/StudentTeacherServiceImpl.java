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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
	public List<User> getCourseAttachedUsers(Long courseId) {
		List<User> users = new ArrayList<>();
		userRepository.findEnrolledUsers(courseId)
				.iterator()
				.forEachRemaining(users::add);
		return users;
	}

	@Override
	public List<User> getCourseNotAttachedUsers(Long courseId) {
		List<User> users = new ArrayList<>();
		// find any users who are not in Course.users -- this will initially include ADMIN users
		userRepository.findNotEnrolledUsers(courseId)
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
			Long userId,
			Long courseId
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole == RoleType.STUDENT) {
			throw new RoleNotSufficientException("Students are not allowed to attach users to courses");
		}
		User   currUser   = userService.findUserById(userId);
		Course currCourse = courseService.findCourseById(courseId);
		if (currUser.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course level");
		} else {
			UserCourses newRelationship = new UserCourses(currUser, currCourse);
			currCourse.getUsers()
					.add(newRelationship);
			courseService.update(currCourse.getCourseId(), currCourse);
		}
	}

	@Override
	public void detachUserFromCourse(
			Long userId,
			Long courseId
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole == RoleType.STUDENT) {
			throw new RoleNotSufficientException("Students are not allowed to detach users to courses");
		}
		User   currUser   = userService.findUserById(userId);
		Course currCourse = courseService.findCourseById(courseId);
		if (currUser.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course level");
		} else {
			boolean removed = currCourse.getUsers()
					.removeIf(userCourses -> userCourses.equals(new UserCourses(currUser, currCourse)));
			if (!removed) {
				throw new ResourceNotFoundException(
						"The user with id " + currUser.getUserId() + " is not part" + " of the course with id + " +
						currCourse.getCourseId());
			}
			courseService.update(currCourse.getCourseId(), currCourse);

		}
	}

	@Override
	public List<User> getCourseAttachedStudents(Long courseId) {
		List<User> students = new ArrayList<>();
		userRepository.findEnrolledUsers(courseId)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.STUDENT) {
						students.add(user);
					}
				});
		return students;
	}

	@Override
	public List<User> getCourseAttachedTeachers(Long courseId) {
		List<User> teachers = new ArrayList<>();
		userRepository.findEnrolledUsers(courseId)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.TEACHER) {
						teachers.add(user);
					}
				});
		return teachers;
	}

	@Override
	public List<User> getCourseDetachedStudents(Long courseId) {
		List<User> students = new ArrayList<>();
		userRepository.findNotEnrolledUsers(courseId)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.STUDENT) {
						students.add(user);
					}
				});
		return students;
	}

	@Override
	public List<User> getCourseDetachedTeachers(Long courseId) {
		List<User> detachedTeachers = new ArrayList<>();
		userRepository.findNotEnrolledUsers(courseId)
				.iterator()
				.forEachRemaining(user -> {
					if (user.getRole() == RoleType.TEACHER) {
						detachedTeachers.add(user);
					}
				});
		return detachedTeachers;
	}

	@Transactional
	@Override
	public User replaceUserEnrollments(
			Long userId,
			List<Long> courseIds
	) {
		User userToUpdate = userService.findUserById(userId);
		if (userToUpdate.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course-level");
		} else {
			// create a hashset from the list passed in (to get only unique ids)
			Set<Long> hashedIds = new HashSet<>(courseIds);
			// this hashset will contain any course that needs to un-enroll the user in question
			Set<UserCourses> coursesToRemoveUser = new HashSet<>();
			userToUpdate.getCourses()
					.forEach(userCourses -> {
						// if the list passed in does not contain the id of a Course
						// that our User was previously enrolled in, we're going to have to
						// remove this user from that course
						if (!hashedIds.contains(userCourses.getCourse()
								.getCourseId())) {
							coursesToRemoveUser.add(userCourses);
						}
					});
			// we must iterate through our courses that we'll remove so
			// that we won't hit a concurrent modification error
			coursesToRemoveUser.forEach(userCourses -> //
					userCourses.getCourse()
							.removeUser(userToUpdate));

			// now, for every course in our hashed ids, we will make a "new"
			// UserCourses instance and add it to our user's courses!
			for (Long courseId : hashedIds) {
				Course      course          = courseService.findCourseById(courseId);
				UserCourses newRelationship = new UserCourses();
				newRelationship.setUser(userToUpdate);
				newRelationship.setCourse(course);
				userToUpdate.getCourses()
						.add(newRelationship);
			}
			// we have been modifying this user straight-up... no need to "save" or "update"
			return userToUpdate;
		}

	}


}
