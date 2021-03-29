package com.lambdaschool.oktafoundation.exceptions;


public class TagNotFoundException
		extends ResourceNotFoundException {

	public TagNotFoundException(long tagId) {
		super("Tag with id " + tagId + " could not be found");
	}

	public TagNotFoundException(String title) {
		super("Tag with title " + title + " could not be found");
	}

	public TagNotFoundException(
			String tagTitle,
			String programName
	) {
		super("Tag with title " + tagTitle + " does not belong to program with name " + programName);
	}

	public TagNotFoundException(
			long tagId,
			long programId
	) {
		super("Tag with id " + tagId + " does not belong to program with id " + programId);
	}

}
