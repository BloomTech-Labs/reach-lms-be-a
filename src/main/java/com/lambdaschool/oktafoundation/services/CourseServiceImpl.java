package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.CourseNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.ProgramNotFoundException;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
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
	private CourseRepository courserepos;

	@Autowired
	private ProgramRepository programrepos;

	@Autowired
	private ProgramService programService;

	@Override
	public List<Course> findAll() {
		List<Course> courses = new ArrayList<>();

		courserepos.findAll()
				.iterator()
				.forEachRemaining(courses::add);

		return courses;
	}

	@Override
	public List<Course> findRelevant() {
		return null;
	}

	@Override
	public List<Course> findRelevant(String query) {
		return null;
	}

	@Override
	public Course findCourseById(long courseid)
	throws CourseNotFoundException {
		return courserepos.findById(courseid)
				.orElseThrow(() -> new CourseNotFoundException(courseid));
	}

	@Override
	public List<Course> findByTag(String tagTitle) {
		List<Course> courses = new ArrayList<>();
		courserepos.findByTags_tag_titleLikeIgnoreCase(tagTitle)
				.iterator()
				.forEachRemaining(courses::add);
		return courses;
	}

	// OVERLOAD FOR CONVENIENCE
	@Override
	public Course save(
			Course course,
			long programid
	) {
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
			newCourse.getTags()
					.clear();
			for (ProgramTags programTags : course.getTags()) {
				newCourse.addTag(programTags.getTag());
			}
		}
		newCourse.getUsers()
				.clear();
		for (UserCourses userCourse : course.getUsers()) {
			newCourse.getUsers()
					.add(new UserCourses(userCourse.getUser(), newCourse));
		}
		return courserepos.save(newCourse);
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
		return courserepos.save(newCourse);
	}

	@Override
	public void delete(long courseid)
	throws CourseNotFoundException {
		findCourseById(courseid); // throws if Course not found
		courserepos.deleteById(courseid);
	}

}
