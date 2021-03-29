package com.lambdaschool.oktafoundation.exceptions;


public class ProgramNotFoundException
		extends ResourceNotFoundException {

	public ProgramNotFoundException(String programName) {
		super("Program with name " + programName + " could not be found.");
	}

	public ProgramNotFoundException(long programId) {
		super("Program with id " + programId + " could not be found.");
	}

}
