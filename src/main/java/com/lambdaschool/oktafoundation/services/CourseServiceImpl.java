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
					courseRepository.search(callingUser.getUserid(), query)
							.iterator()
							.forEachRemaining(courses::add);
				} else {
					return findByUser(callingUser.getUserid());
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
	public List<Course> findByUser(long userid) {
		return new ArrayList<>(courseRepository.findCoursesByUserid(userid));
	}

	@Override
	public Course findCourseById(long courseid)
	throws CourseNotFoundException {
		return get(courseid);
	}

	@Override
	public Course get(long courseid)
	throws CourseNotFoundException {
		return courseRepository.findById(courseid)
				.orElseThrow(() -> new CourseNotFoundException(courseid));
	}

	@Override
	public Course get(String coursename)
	throws CourseNotFoundException {
		return courseRepository.findByCoursename(coursename)
				.orElseThrow(() -> new CourseNotFoundException(coursename));
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
					.getProgramid(), course);
		} else {
			throw new ProgramNotFoundException("Anonymous/Null Program");
		}
	}

	// OVERLOAD FOR CONVENIENCE
	@Override
	public Course save(
			Course course,
			long programid
	)
	throws ProgramNotFoundException {
		return save(programid, course);
	}

	@Override
	public Course save(
			long programid,
			Course course
	)
	throws ProgramNotFoundException, CourseNotFoundException {
		Course newCourse = new Course();
		if (course.getCourseid() != 0) {
			findCourseById(course.getCourseid()); // throws if course not found
			newCourse.setCourseid(course.getCourseid());
		}
		newCourse.setCoursename(course.getCoursename());
		newCourse.setCoursedescription(course.getCoursedescription());
		newCourse.setCoursecode(course.getCoursecode());
		newCourse.getModules()
				.clear();
		for (Module module : course.getModules()) {
			newCourse.getModules()
					.add(new Module(module.getModulename(), module.getModuledescription(), module.getModulecontent(), newCourse));
		}
		Program program = programService.findProgramsById(programid); // throws if program not found
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
			long courseid,
			Course course
	) {
		Course newCourse = findCourseById(courseid); // throws if Course not found

		if (course.getCoursename() != null) {
			newCourse.setCoursename(course.getCoursename());
		}
		if (course.getCoursedescription() != null) {
			newCourse.setCoursedescription(course.getCoursedescription());
		}
		if (course.getCoursecode() != null) {
			newCourse.setCoursecode(course.getCoursecode());
		}
		return courseRepository.save(newCourse);
	}

	@Override
	public void delete(long courseid)
	throws CourseNotFoundException {
		findCourseById(courseid); // throws if Course not found
		courseRepository.deleteById(courseid);
	}

}
