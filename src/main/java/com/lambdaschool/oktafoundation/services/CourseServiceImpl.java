package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.CourseNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.ProgramNotFoundException;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(value = "courseServices")
@Transactional
public class CourseServiceImpl
		implements CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ProgramService programService;

	@Autowired
	private HelperFunctions helperFunctions;

	@Override
	public List<Course> findAll() {
		List<Course> courses = new ArrayList<>();

		courseRepository.findAll()
				.iterator()
				.forEachRemaining(courses::add);

		return courses;
	}

	@Override
	public List<Course> findRelevant(String query) {
		List<Course> courses     = new ArrayList<>();
		User         callingUser = helperFunctions.getCallingUser();
		RoleType     callingRole = callingUser.getRole();

		// switch based on the role of the calling user
		switch (callingRole) {
			// if the calling user is STUDENT or a TEACHER, we want to return any courses
			// that they are attached to.
			// NOTE that TEACHER and STUDENT have the same result, so we merge their cases
			// by stacking `case TEACHER:` right on top of `case STUDENT:`
			case TEACHER: // stacking case statements is how you merge cases in Java
			case STUDENT: // stacking case statements is how you merge cases in Java
				if (query != null) {
					courseRepository.search(callingUser.getUserId(), query)
							.iterator()
							.forEachRemaining(courses::add);
				} else {
					return findByUser(callingUser.getUserId());
				}
				break;
			// if the calling user is an ADMIN, we want to return all courses in the system.
			// OR DEFAULT -- if somehow the calling user doesn't have a role, we're currently
			// defaulting to all the courses in the system!
			// This is something that may be worth changing later on for security reasons.
			case ADMIN:
			default:
				if (query != null) {
					courseRepository.search(query)
							.iterator()
							.forEachRemaining(courses::add);
				} else {
					return findAll();
				}
				break;
		}
		// return the courses that are relevant to this user!!
		// if we reach this line, it means the user DID provide a query!
		return courses;
	}

	@Override
	public List<Course> findByUser(long userId) {
		return new ArrayList<>(courseRepository.findCoursesByUserId(userId));
	}

	@Override
	public Course findCourseById(long courseId)
	throws CourseNotFoundException {
		return get(courseId);
	}

	@Override
	public Course get(long courseId)
	throws CourseNotFoundException {
		return courseRepository.findById(courseId)
				.orElseThrow(() -> new CourseNotFoundException(courseId));
	}

	@Override
	public Course get(String courseName)
	throws CourseNotFoundException {
		return courseRepository.findByCourseName(courseName)
				.orElseThrow(() -> new CourseNotFoundException(courseName));
	}

	@Override
	public List<Course> findByTag(String tagTitle) {
		List<Course> courses = new ArrayList<>();

		courseRepository.findByTag_tag_titleLikeIgnoreCase(tagTitle)
				.iterator()
				.forEachRemaining(courses::add);
		return courses;
	}

	// OVERLOAD FOR CONVENIENCE
	@Override
	public Course save(Course course)
	throws ProgramNotFoundException {
		if (course.getProgram() != null) {
			return save(course.getProgram()
					.getProgramId(), course);
		} else {
			throw new ProgramNotFoundException("Anonymous/Null Program");
		}
	}

	// OVERLOAD FOR CONVENIENCE
	@Override
	public Course save(
			Course course,
			long programId
	)
	throws ProgramNotFoundException {
		return save(programId, course);
	}

	@Override
	public Course save(
			long programId,
			Course course
	)
	throws ProgramNotFoundException, CourseNotFoundException {
		Course newCourse = new Course();
		if (course.getCourseId() != 0) {
			findCourseById(course.getCourseId()); // throws if course not found
			newCourse.setCourseId(course.getCourseId());
		}
		newCourse.setCourseName(course.getCourseName());
		newCourse.setCourseDescription(course.getCourseDescription());
		newCourse.setCourseCode(course.getCourseCode());
		newCourse.getModules()
				.clear();
		for (Module module : course.getModules()) {
			newCourse.getModules()
					.add(new Module(module.getModuleName(), module.getModuleDescription(), module.getModuleContent(), newCourse));
		}
		Program program = programService.findProgramsById(programId); // throws if program not found
		if (program != null) {
			newCourse.setProgram(program);
		}
		if (course.getTag() != null) {
			newCourse.setTag(course.getTag());
		}
		newCourse.getUsers()
				.clear();
		for (UserCourses userCourse : course.getUsers()) {
			newCourse.getUsers()
					.add(new UserCourses(userCourse.getUser(), newCourse));
		}
		return courseRepository.save(newCourse);
	}

	@Override
	public Course update(
			long courseId,
			Course course
	) {
		Course newCourse = findCourseById(courseId); // throws if Course not found

		if (course.getCourseName() != null) {
			newCourse.setCourseName(course.getCourseName());
		}
		if (course.getCourseDescription() != null) {
			newCourse.setCourseDescription(course.getCourseDescription());
		}
		if (course.getCourseCode() != null) {
			newCourse.setCourseCode(course.getCourseCode());
		}
		return courseRepository.save(newCourse);
	}

	@Override
	public void delete(long courseId)
	throws CourseNotFoundException {
		findCourseById(courseId); // throws if Course not found
		courseRepository.deleteById(courseId);
	}

	@Override
	public void deleteAll() {
		courseRepository.deleteAll();
	}

}
