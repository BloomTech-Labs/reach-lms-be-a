package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.controllers.UserController;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler
		implements RepresentationModelAssembler<User, EntityModel<User>> {

	@Autowired
	HelperFunctions helperFunctions;

	@Override
	public EntityModel<User> toModel(User user) {
		EntityModel<User> userEntityModel = EntityModel.of(user,
				// Link to SELF --- GET /users/user/{userid}
				linkTo(methodOn(UserController.class).getUserById(user.getUserid())).withSelfRel(),
				// Link to self by name --- GET /users/user/name/{username}
				linkTo(methodOn(UserController.class).getUserByName(user.getUsername())).withRel("self_by_name")
		);

		// this will hold the role of the CALLING USER -- whomever is hitting this endpoint should have a role
		RoleType callingUser = helperFunctions.getCurrentPriorityRole();

		// this will hold the role of the user to be converted into a model
		RoleType usersRole = user.getRole();

		// if the user to convert to a model is a STUDENT, add the following links
		if (usersRole == RoleType.STUDENT) {
			userEntityModel.add(linkTo(methodOn(CourseController.class).getStudentCourses(user.getUserid())).withRel("courses"));

			if (callingUser == RoleType.ADMIN) {
				userEntityModel.add(linkTo(methodOn(CourseController.class).getUserAntiCourses(user.getUserid())).withRel(
						"available_courses"),
						linkTo(methodOn(CourseController.class).getMappifiedCoursesByUser(user.getUserid())).withRel(
								"mappified_courses")
				);
			}
		}

		// if the user to convert to a model is a TEACHER, add the following links
		if (usersRole == RoleType.TEACHER) {
			userEntityModel.add(linkTo(methodOn(CourseController.class).getTeacherCourses(user.getUserid())).withRel("courses"));
			if (callingUser == RoleType.ADMIN) {
				userEntityModel.add(linkTo(methodOn(CourseController.class).getUserAntiCourses(user.getUserid())).withRel(
						"available_courses"),
						linkTo(methodOn(CourseController.class).getMappifiedCoursesByUser(user.getUserid())).withRel(
								"mappified_courses")
				);
			}
		}

		// if the user to convert to a model is an ADMIN, add the following links
		if (usersRole == RoleType.ADMIN) {
			// Link to GET Programs by User.userid
			userEntityModel.add( //
					linkTo(methodOn(ProgramController.class).getProgramsByUserId(user.getUserid())).withRel("programs"));
		}

		// if the calling user is an admin and the user in question is NOT an admin
		if (callingUser == RoleType.ADMIN && user.getRole() != RoleType.ADMIN) {
			userEntityModel.add(
					// Link to DELETE User by User.userid
					linkTo(methodOn(UserController.class).deleteUserById(user.getUserid())).withRel("delete_user"),
					linkTo(methodOn(UserController.class).updateFullUser(null, user.getUserid())).withRel("replace_user"),
					linkTo(methodOn(UserController.class).updateUser(null, user.getUserid())).withRel("edit_user"),
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.STUDENT)).withRel(
							"make_student"),
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.ADMIN)).withRel("make_admin"),
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.TEACHER)).withRel(
							"make_teacher")
			);
		}

		return userEntityModel;
	}

}
