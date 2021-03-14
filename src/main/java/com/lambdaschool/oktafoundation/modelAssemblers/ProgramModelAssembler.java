package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class ProgramModelAssembler
		implements RepresentationModelAssembler<Program, EntityModel<Program>> {

	@Override
	public EntityModel<Program> toModel(Program entity) {
		return null;
	}

}
