package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.models.Teacher;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;


public class TeacherModelAssembler implements RepresentationModelAssembler<Teacher, EntityModel<Teacher>> {

	@Override
	public EntityModel<Teacher> toModel(Teacher entity) {
		return null;
	}

}
