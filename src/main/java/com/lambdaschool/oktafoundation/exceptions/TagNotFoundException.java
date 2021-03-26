package com.lambdaschool.oktafoundation.exceptions;


public class TagNotFoundException
		extends ResourceNotFoundException {

	public TagNotFoundException(long tagid) {
		super("Tag with id " + tagid + " could not be found");
	}

	public TagNotFoundException(String title) {
		super("Tag with title " + title + " could not be found");
	}

}
