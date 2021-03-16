package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ModuleController;
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
		return EntityModel.of(module,
				linkTo(methodOn(ModuleController.class).getModuleById(module.getModuleid())).withSelfRel(),
				linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"),
				linkTo(methodOn(CourseController.class).getCourseByCourseId(module.getCourse()
						.getCourseid())).withRel("course")
		);
	}
	

}
