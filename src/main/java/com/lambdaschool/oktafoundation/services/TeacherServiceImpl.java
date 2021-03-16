package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Teacher;
import com.lambdaschool.oktafoundation.models.TeacherCourses;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service(value = "teacherService")
public class TeacherServiceImpl
		implements TeacherService {

	@Autowired
	private TeacherRepository teacherrepos;

	@Autowired
	private CourseRepository courserepos;

	@Override
	public List<Teacher> findAll() {
		List<Teacher> teachers = new ArrayList<>();
		teacherrepos.findAll()
				.iterator()
				.forEachRemaining(teachers::add);

		return teachers;
	}

	@Override
	public Teacher update(
			Teacher teacher,
			long courseid
	) {
		Teacher currTeacher = teacherrepos.findTeacherByTeachername(teacher.getTeachername());
		Course currCourse = courserepos.findById(courseid)
				.orElseThrow(() -> new ResourceNotFoundException("Course with id " + courseid + " Not found!"));


		currTeacher.getCourses()
				.add(new TeacherCourses(currTeacher, currCourse));


		return teacherrepos.save(currTeacher);

	}

}
