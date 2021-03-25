package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class RolesServiceImpl
		implements RolesService {

	@Autowired
	HelperFunctions helperFunctions;
	@Autowired
	Roles           roles;

	@Override
	@PreAuthorize("hasRole(@roles.ADMIN)")
	public boolean ensureAdmin() {
		return true;
	}

	@Override
	@PreAuthorize("hasRole(@roles.TEACHER)")
	public boolean ensureTeacher() {
		return true;
	}

	@Override
	@PreAuthorize("hasRole(@roles.STUDENT)")
	public boolean ensureStudent() {
		return true;
	}

	@Override
	@PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.TEACHER)")
	public boolean ensureAdminOrTeacher() {
		return true;
	}

	@PreAuthorize("hasRole(@roles.ADMIN)")
	public boolean canWriteProgram(Program program) {
		if (program.getUser()
				    .getUserid() == helperFunctions.getCallingUser()
				    .getUserid()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@PreAuthorize(("hasAnyRole(@roles.ADMIN, @roles.TEACHER)"))
	public boolean canWriteCourse(Course course) {
		User callingUser = helperFunctions.getCallingUser();
		boolean isAdmin = callingUser.getRole()
				.name()
				.equals(roles.ADMIN);
		boolean isCourseOwnedByUser = course.getProgram()
				                              .getUser()
				                              .getUserid() == callingUser.getUserid();
		if (isAdmin) {
			return isCourseOwnedByUser;
		} else {
			return course.getUsers()
					.contains(new UserCourses(callingUser, course));

		}
	}

}
