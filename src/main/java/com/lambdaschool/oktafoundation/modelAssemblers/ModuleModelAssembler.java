package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ModuleController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.models.Module;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ModuleModelAssembler
		implements RepresentationModelAssembler<Module, EntityModel<Module>> {

	@Override
	public EntityModel<Module> toModel(Module module) {

		EntityModel<Module> moduleEntity = EntityModel.of(module,
				// Link to SELF --- GET /modules/module/{moduleid}
				linkTo(methodOn(ModuleController.class).getModuleById(module.getModuleid())).withSelfRel(),
				// Link to all_modules --- GET /modules/modules
				linkTo(methodOn(ModuleController.class).getAllModules()).withRel("all_modules"),
				// Link to associated course --- GET /courses/course/{courseid}
				linkTo(methodOn(CourseController.class).getCourseByCourseId(module.getCourse()
						.getCourseid())).withRel("course"),
				// Link to associated program --- GET /programs/program/{programid}
				linkTo(methodOn(ProgramController.class).getProgramById(module.getCourse()
						.getProgram()
						.getProgramid())).withRel("program")
		);


		return moduleEntity;
	}


}
