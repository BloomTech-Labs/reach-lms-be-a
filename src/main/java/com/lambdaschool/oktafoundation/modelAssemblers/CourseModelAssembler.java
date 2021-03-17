package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ModuleController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.models.Course;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * A helper component able to transform a Course model into a restful Representation Model with
 * relative links.
 */
@Component
public class CourseModelAssembler
		implements RepresentationModelAssembler<Course, EntityModel<Course>> {

	/**
	 * Creates a RepresentationModel for the given Course with useful
	 *
	 * @param course The course to transform into a representational model
	 *
	 * @return An EntityModel<Course> that includes our basic model with additional relative links
	 */
	@Override
	public EntityModel<Course> toModel(Course course) {
		return EntityModel.of(course,
				// link to self (ALWAYS)
				linkTo(methodOn(CourseController.class).getCourseByCourseId(course.getCourseid())).withSelfRel(),
				// link to associated program
				linkTo(methodOn(ProgramController.class).getProgramById(course.getProgram()
						.getProgramid())).withRel("program"),
				// link to all courses
				linkTo(methodOn(CourseController.class).getAllCourses()).withRel("all_courses"),
				// link to associated modules
				linkTo(methodOn(ModuleController.class).getModulesByCourseId(course.getCourseid())).withRel("modules")

		);
	}


}
