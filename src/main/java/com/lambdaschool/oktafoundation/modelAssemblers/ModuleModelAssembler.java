package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ModuleController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ModuleModelAssembler
		implements RepresentationModelAssembler<Module, EntityModel<Module>> {

	@Autowired
	private HelperFunctions helperFunctions;

	@Override
	public EntityModel<Module> toModel(Module module) {
		EntityModel<Module> moduleEntity = EntityModel.of(module,

				// Link to SELF
				// GET /modules/module/{moduleid}
				linkTo(methodOn(ModuleController.class).getModuleById(module.getModuleid())).withSelfRel(),

				// Link to all_modules
				// GET /modules/modules
				linkTo(methodOn(ModuleController.class).getAllModules()).withRel("all_modules"),

				// Link to associated course
				// GET /courses/course/{courseid}
				linkTo(methodOn(CourseController.class).getCourseByCourseId(module.getCourse()
						.getCourseid())).withRel("course"),

				// Link to associated program
				// GET /programs/program/{programid}
				linkTo(methodOn(ProgramController.class).getProgramById(module.getCourse()
						.getProgram()
						.getProgramid())).withRel("program"),

				// Link to the Markdown content for this module
				// GET /modules/markdown/{moduleid}
				linkTo(methodOn(ModuleController.class).getMarkdownByModuleId(module.getModuleid())).withRel("markdown")
		);

		// get the calling user's role!!
		RoleType callingRole = helperFunctions.getCurrentPriorityRole();

		// if the calling user is an ADMIN or a TEACHER -- add the following links
		if (callingRole == RoleType.ADMIN || callingRole == RoleType.TEACHER) {

			moduleEntity.add(
					// link to DELETE self
					linkTo(methodOn(ModuleController.class).deleteModuleById(module.getModuleid())).withRel("delete_module"),
					// link to PATCH self
					linkTo(methodOn(ModuleController.class).updateModule(module.getModuleid(), null)).withRel("edit_module"),
					// Link to PUT self
					linkTo(methodOn(ModuleController.class).updateModule(module.getModuleid(), null)).withRel("replace_module"),
					// Link to Replace markdown
					linkTo(methodOn(ModuleController.class).replaceMarkdownByModuleId(module.getModuleid(), null)).withRel(
							"replace_markdown")
			);

			try {
				moduleEntity.add(//
						// link to POST module to the same course that this module belongs to
						linkTo(methodOn(ModuleController.class).addNewModule(module.getCourse()
								.getCourseid(), null)).withRel("add_module_sibling")
				);
			} catch (Exception ignored) {}
		}

		return moduleEntity;
	}


}
