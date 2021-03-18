package com.lambdaschool.oktafoundation.modelAssemblers;


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
				// Link to SELF --- GET /users/user/name/{username}
				linkTo(methodOn(UserController.class).getUserByName(user.getUsername())).withSelfRel(),
				// Link to GET Programs by User.userid
				linkTo(methodOn(ProgramController.class).getProgramsByUserId(user.getUserid())).withRel("programs")
		);

		RoleType currentRole = helperFunctions.getCurrentPriorityRole();

		if (currentRole == RoleType.ADMIN) {
			userEntityModel.add(
					// Link to DELETE User by User.userid
					linkTo(methodOn(UserController.class).deleteUserById(user.getUserid())).withRel("delete"),
					// Link to GET all users
					linkTo(methodOn(UserController.class).listAllUsers()).withRel("all_users"),
					// link to POST Program (with userId)
					linkTo(methodOn(ProgramController.class).addNewProgram(user.getUserid(), null)).withRel("post_program")
			);
		}

		return userEntityModel;
	}

}
