package com.lambdaschool.oktafoundation.exceptions;


public class RoleNotSufficientException
		extends RuntimeException {

	public RoleNotSufficientException(String message) {
		super(String.format("Error %s", message));
	}

}
