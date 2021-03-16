package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.StudentController;
import com.lambdaschool.oktafoundation.models.Student;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class StudentModelAssembler
		implements RepresentationModelAssembler<Student, EntityModel<Student>> {

	@Override
	public EntityModel<Student> toModel(Student student) {
		return EntityModel.of(student,
				linkTo(methodOn(StudentController.class).getStudentByName(student.getStudentname())).withSelfRel(),
				linkTo(methodOn(StudentController.class).getAllStudents()).withRel("all_students"),
				linkTo(methodOn(CourseController.class).getStudentCourses(student.getStudentid())).withRel("student_courses")
		);
	}

}
