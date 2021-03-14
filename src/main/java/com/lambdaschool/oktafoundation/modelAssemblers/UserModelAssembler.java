package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.models.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class UserModelAssembler
		implements RepresentationModelAssembler<User, EntityModel<User>> {

	@Override
	public EntityModel<User> toModel(User entity) {
		return null;
	}

}
