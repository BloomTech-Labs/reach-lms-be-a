package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.controllers.TagController;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ProgramModelAssembler
		implements RepresentationModelAssembler<Program, EntityModel<Program>> {

	@Autowired
	HelperFunctions helperFunctions;

	@Override
	public EntityModel<Program> toModel(Program program) {
		EntityModel<Program> programEntity = EntityModel.of(program,
				// Link to SELF --- GET /programs/program/{programid}
				linkTo(methodOn(ProgramController.class).getProgramById(program.getProgramid())).withSelfRel(),
				// Link to self_by_name --- GET /programs/program/{programname}
				linkTo(methodOn(ProgramController.class).getProgramByName(program.getProgramname())).withRel("self_by_name"),
				// Link to associated courses
				linkTo(methodOn(CourseController.class).getCoursesByProgramid(program.getProgramid())).withRel("courses"),
				// Link to GET all tags associated with this program
				linkTo(methodOn(TagController.class).getByProgram(program.getProgramid())).withRel("tags")
		);

		RoleType currentRole = helperFunctions.getCurrentPriorityRole();

		if (currentRole == RoleType.ADMIN) {
			programEntity.add(
					// Link to GET all programs by this program's admin
					linkTo(methodOn(ProgramController.class).getProgramsByUserId(program.getUser()
							.getUserid())).withRel("admin_programs"),
					// Link to GET all programs
					linkTo(methodOn(ProgramController.class).listAllPrograms()).withRel("all_programs"),
					// Link to DELETE self
					linkTo(methodOn(ProgramController.class).deleteProgram(program.getProgramid())).withRel("delete_program"),
					// Link to PATCH self
					linkTo(methodOn(ProgramController.class).editPartialProgram(program, program.getProgramid())).withRel(
							"edit_program"),
					// Link to PUT self
					linkTo(methodOn(ProgramController.class).editEntireProgram(program, program.getProgramid())).withRel(
							"replace_program"),
					// Link to POST new program (with the same admin that is associated with this course)
					linkTo(methodOn(ProgramController.class).addNewProgram(program.getUser()
							.getUserid(), program)).withRel("add_program")
			);
		}

		return programEntity;
	}

}
