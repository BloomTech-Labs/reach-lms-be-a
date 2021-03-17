package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.TeacherController;
import com.lambdaschool.oktafoundation.models.Teacher;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeacherModelAssembler
		implements RepresentationModelAssembler<Teacher, EntityModel<Teacher>> {

	@Override
	public EntityModel<Teacher> toModel(Teacher teacher) {
		return EntityModel.of(teacher,
				// Link to SELF
				linkTo(methodOn(TeacherController.class).getTeacherByName(teacher.getTeachername())).withSelfRel(),
				// Link to "GET ALL TEACHERS"
				linkTo(methodOn(TeacherController.class).getAllTeachers()).withRel("all_teachers"),
				linkTo(methodOn(CourseController.class).getTeacherCourses(teacher.getTeacherid())).withRel("courses")
		);
	}

}
