package com.lambdaschool.oktafoundation.exceptions;


public class ModuleNotFoundException
		extends ResourceNotFoundException {

	public ModuleNotFoundException(long moduleId) {
		super("Module with id " + moduleId + " could not be found.");
	}

	public ModuleNotFoundException(String moduleName) {
		super("Module with name " + moduleName + " could not be found.");
	}

}
