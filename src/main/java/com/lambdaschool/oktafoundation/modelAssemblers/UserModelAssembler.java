package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.controllers.UserController;
import com.lambdaschool.oktafoundation.models.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler
		implements RepresentationModelAssembler<User, EntityModel<User>> {

	@Override
	public EntityModel<User> toModel(User user) {


		return EntityModel.of(user,
				// Link to SELF by User.userid
				linkTo(methodOn(UserController.class).getUserById(user.getUserid())).withSelfRel(),
				// Link to SELF by User.username
				linkTo(methodOn(UserController.class).getUserByName(user.getUsername())).withSelfRel(),
				// Link to DELETE User by User.userid
				linkTo(methodOn(UserController.class).deleteUserById(user.getUserid())).withRel("delete"),
				// Link to GET all users
				linkTo(methodOn(UserController.class).listAllUsers()).withRel("all_users"),
				// Link to GET Programs by User.userid
				linkTo(methodOn(ProgramController.class).getProgramsByUserId(user.getUserid())).withRel("programs"),
				// link to POST Program (with userId)
				linkTo(methodOn(ProgramController.class).addNewProgram(user.getUserid(), null)).withRel("post_program")
		);
	}

}
