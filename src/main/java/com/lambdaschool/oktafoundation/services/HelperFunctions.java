package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.ValidationError;

import java.util.List;


/**
 * Class contains helper functions - functions that are needed throughout the application. The class can be autowired
 * into any class.
 */
public interface HelperFunctions {

	/**
	 * Searches to see if the exception has any constraint violations to report
	 *
	 * @param cause the exception to search
	 *
	 * @return constraint violations formatted for sending to the client
	 */
	List<ValidationError> getConstraintViolation(Throwable cause);

	/**
	 * Checks to see if the authenticated user has access to modify the requested user's information
	 *
	 * @param username The user name of the user whose data is requested to be changed. This should either match the authenticated user
	 *                 or the authenticate must have the role ADMIN
	 *
	 * @return true if the user can make the modifications, otherwise an exception is thrown
	 */
	boolean isAuthorizedToMakeChange(String username);

	RoleType getCurrentPriorityRole();

	User getCallingUser();

	// THIS may be a good/simple/customizable solution to adding
	// some granularity & consistency to the permissions in our app
	//
	//	boolean isAuthorizedToMakeChange(RoleType role);
	//	boolean isAuthorizedToMakeChange(
	//			String username,
	//			User user
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			String username,
	//			Program program
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			String username,
	//			Course course
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			String username,
	//			Module module
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			RoleType role,
	//			User user
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			RoleType role,
	//			Program program
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			RoleType role,
	//			Course course
	//	);
	//	boolean isAuthorizedToMakeChange(
	//			RoleType role,
	//			Module module
	//	);


}
