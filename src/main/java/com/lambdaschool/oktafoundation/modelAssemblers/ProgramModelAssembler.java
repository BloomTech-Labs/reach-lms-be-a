package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ProgramModelAssembler
		implements RepresentationModelAssembler<Program, EntityModel<Program>> {

	@Override
	public EntityModel<Program> toModel(Program program) {
		return EntityModel.of(program,
				// Link to self by `programid`
				linkTo(methodOn(ProgramController.class).getProgramById(program.getProgramid())).withSelfRel(),
				// Link to self by `programname`
				linkTo(methodOn(ProgramController.class).getProgramByName(program.getProgramname())).withSelfRel(),
				// Link to all programs by this program's admin
				linkTo(methodOn(ProgramController.class).getProgramsByUserId(program.getUser()
						.getUserid())).withRel("admin_programs"),
				// Link to [GET all programs]
				linkTo(methodOn(ProgramController.class).listAllPrograms()).withRel("all_programs")

		);
	}

}
