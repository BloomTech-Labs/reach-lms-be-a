package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service(value = "helperFunctions")
public class HelperFunctionsImpl
		implements HelperFunctions {

	@Autowired
	UserService userService;

	public List<ValidationError> getConstraintViolation(Throwable cause) {
		// Find any data violations that might be associated with the error and report them
		// data validations get wrapped in other exceptions as we work through the Spring
		// exception chain. Hence we have to search the entire Spring Exception Stack
		// to see if we have any violation constraints.
		while ((cause != null) &&
		       !(cause instanceof ConstraintViolationException || cause instanceof MethodArgumentNotValidException)) {
			System.out.println(cause.getClass()
					.toString());
			cause = cause.getCause();
		}

		List<ValidationError> listVE = new ArrayList<>();

		// we know that cause either null or an instance of ConstraintViolationException
		if (cause != null) {
			if (cause instanceof ConstraintViolationException) {
				ConstraintViolationException ex    = (ConstraintViolationException) cause;
				ValidationError              newVe = new ValidationError();
				newVe.setCode(ex.getMessage());
				newVe.setMessage(ex.getConstraintName());
				listVE.add(newVe);
			} else {
				if (cause instanceof MethodArgumentNotValidException) {
					MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;
					List<FieldError> fieldErrors = ex.getBindingResult()
							.getFieldErrors();
					for (FieldError err : fieldErrors) {
						ValidationError newVe = new ValidationError();
						newVe.setCode(err.getField());
						newVe.setMessage(err.getDefaultMessage());
						listVE.add(newVe);
					}
				} else {
					System.out.println("Error in producing constraint violations exceptions. " +
					                   "If we see this in the console a major logic error has occurred in the " +
					                   "helperfunction.getConstraintViolation method that we should investigate. " +
					                   "Note the application will keep running as this only affects exception reporting!");
				}
			}
		}
		return listVE;
	}

	@Override
	public boolean isAuthorizedToMakeChange(String username) {
		// Check to see if the user whose information being requested is the current user
		// Check to see if the requesting user is an admin
		// if either is true, return true
		// otherwise stop the process and throw an exception
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (username.equalsIgnoreCase(authentication.getName()
				.toLowerCase()) || authentication.getAuthorities()
				    .contains(new SimpleGrantedAuthority("ADMIN"))) {
			// this user can make this change
			return true;
		} else {
			// stop user is not authorized to make this change so stop the whole process and throw an exception
			throw new ResourceNotFoundException(authentication.getName() + " not authorized to make change");
		}
	}

	@Override
	public RoleType getCurrentPriorityRole() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (authorities.contains(new SimpleGrantedAuthority(RoleType.ADMIN.name()))) {
			return RoleType.ADMIN;
		} else if (authorities.contains(new SimpleGrantedAuthority(RoleType.TEACHER.name()))) {
			return RoleType.TEACHER;
		} else if (authorities.contains(new SimpleGrantedAuthority(RoleType.STUDENT.name()))) {
			return RoleType.STUDENT;
		} else {
			// default case... this is mostly just for testing, as we usually shouldn't hit this default in production
			// when testing feel free to change this around to see what TEACHER and STUDENT might see
			return RoleType.ADMIN;
		}
	}

	@Override
	public User getCallingUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		return userService.findByName(authentication.getName());
	}

}
