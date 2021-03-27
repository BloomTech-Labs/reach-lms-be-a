package com.lambdaschool.oktafoundation.exceptions;


import com.lambdaschool.oktafoundation.models.Program;


public class TagFoundException
		extends ResourceFoundException {

	public TagFoundException(String title) {
		super("Tag with title " + title + " already exists!");
	}

	public TagFoundException(
			String title,
			Program program
	) {
		super("Tag with title " + title + " already exists in program " + program.getProgramname());
	}

	public TagFoundException(long tagId) {
		super("Tag with id " + tagId + " already exists!");
	}

	public TagFoundException(
			long tagId,
			Program program
	) {
		super("Tag with id " + tagId + " already exists in program with id " + program.getProgramid());
	}

}
