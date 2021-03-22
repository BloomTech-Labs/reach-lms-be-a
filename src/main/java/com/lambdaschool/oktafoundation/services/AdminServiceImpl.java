package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl
		implements AdminService {

	@Autowired
	HelperFunctions helperFunctions;

	@Override
	@PreAuthorize("hasRole(@roles.ADMIN)")
	public boolean ensureAdmin() {
		return true;
	}

	@PreAuthorize("hasRole(@roles.ADMIN)")
	public boolean hasOwnership(Program program) {
		if (program.getUser().getUserid() == helperFunctions.getCallingUser().getUserid()) {
			return true;
		} else {
			return false;
		}
	}
}
