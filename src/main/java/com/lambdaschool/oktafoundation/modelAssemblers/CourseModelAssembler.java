package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.*;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private HelperFunctions helperFunctions;

	/**
	 * Creates a RepresentationModel for the given Course with useful
	 *
	 * @param course The course to transform into a representational model
	 *
	 * @return An EntityModel<Course> that includes our basic model with additional relative links
	 */
	@Override
	public EntityModel<Course> toModel(Course course) {

		EntityModel<Course> courseEntityModel = EntityModel.of(course,
				// Link to SELF --- GET /courses/course/{courseid}
				linkTo(methodOn(CourseController.class).getCourseByCourseId(course.getCourseid())).withSelfRel(),
				// Link to associated program --- GET /programs/program/{programid}
				linkTo(methodOn(ProgramController.class).getProgramById(course.getProgram()
						.getProgramid())).withRel("program"),
				// Link to all courses --- GET /courses
				linkTo(methodOn(CourseController.class).getAllCourses()).withRel("all_courses"),
				// Link to associated modules --- GET /modules/module/{moduleid}
				linkTo(methodOn(ModuleController.class).getModulesByCourseId(course.getCourseid())).withRel("modules")

		);

		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		// if the calling user is an ADMIN or a TEACHER, display the following additional links
		if (callingUserRole == RoleType.ADMIN || callingUserRole == RoleType.TEACHER) {
			courseEntityModel.add(
					// Link to associated users --- GET /courses/course/{courseid}/enrolled
					linkTo(methodOn(StudentTeacherController.class).getAllEnrolled(course.getCourseid())).withRel("enrolled_users"),

					// Link to all enrolled teachers --- GET /courses/course/{courseid}/enrolled-teachers
					linkTo(methodOn(StudentTeacherController.class).getEnrolledTeachers(course.getCourseid())).withRel(
							"enrolled_teachers"),

					// Link to all enrolled students --- GET /courses/course/{courseid}/enrolled-students
					linkTo(methodOn(StudentTeacherController.class).getEnrolledStudents(course.getCourseid())).withRel(
							"enrolled_students"),

					// Link to all non-associated users --- GET /courses/course/{courseid}/detached
					linkTo(methodOn(StudentTeacherController.class).getAllNotEnrolled(course.getCourseid())).withRel(
							"available_users"),

					// Link to all non-associated teachers --- GET /courses/course/{courseid}/detached-teachers
					linkTo(methodOn(StudentTeacherController.class).getDetachedTeachers(course.getCourseid())).withRel(
							"available_teachers"),

					// Link to all non-associated students --- GET /courses/course/{courseid}/detached-students
					linkTo(methodOn(StudentTeacherController.class).getDetachedStudents(course.getCourseid())).withRel(
							"available_students")
			);
			try {
				courseEntityModel.add(
						// Link to ADD new module
						linkTo(methodOn(ModuleController.class).addNewModule(course.getCourseid(), null)).withRel("add_module"));
			} catch (Exception ignored) {}
		}

		// if the calling user is an ADMIN, display the following additional links
		if (callingUserRole == RoleType.ADMIN) {
			courseEntityModel.add(
					// Link to PATCH course
					linkTo(methodOn(CourseController.class).updateCourse(course.getCourseid(), null)).withRel("edit_course"),

					// Link to PUT course
					linkTo(methodOn(CourseController.class).updateFullCourse(course.getCourseid(),
							null
					)).withRel("replace_course"),

					// Link to DELETE course
					linkTo(methodOn(CourseController.class).deleteCourseById(course.getCourseid())).withRel("delete_course")
			);
		}

		return courseEntityModel;
	}

}
