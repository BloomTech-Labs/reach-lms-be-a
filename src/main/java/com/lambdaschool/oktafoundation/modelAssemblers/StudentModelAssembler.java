package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.models.Student;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class StudentModelAssembler
		implements RepresentationModelAssembler<Student, EntityModel<Student>> {

	@Override
	public EntityModel<Student> toModel(Student entity) {
		return null;
	}

}
