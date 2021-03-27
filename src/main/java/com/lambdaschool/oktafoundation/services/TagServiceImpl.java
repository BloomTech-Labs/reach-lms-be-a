package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ProgramNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.TagFoundException;
import com.lambdaschool.oktafoundation.exceptions.TagNotFoundException;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.ProgramTags;
import com.lambdaschool.oktafoundation.models.Tag;
import com.lambdaschool.oktafoundation.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TagServiceImpl
		implements TagService {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private ProgramService programService;

	@Override
	public List<Tag> getAll() {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll()
				.iterator()
				.forEachRemaining(tags::add);
		return tags;
	}

	@Override
	public Tag get(long tagid) {
		return tagRepository.findById(tagid)
				.orElseThrow(() -> new TagNotFoundException(tagid));
	}

	@Override
	public Tag get(String title) {
		return null;
	}

	@Override
	public List<Tag> getByProgram(long programid) {
		return null;
	}

	@Override
	public List<Tag> getByProgram(String programname) {
		return null;
	}

	@Override
	public List<Tag> getByCourse(long courseid) {
		return null;
	}

	@Override
	public List<Tag> getByCourse(String coursename) {
		return null;
	}

	@Override
	public Tag save(
			long programid,
			Tag tag
	)
	throws TagNotFoundException, TagFoundException, ProgramNotFoundException {
		Program program = programService.findProgramsById(programid); // throws if program does not exist

		Tag newTag = new Tag();


		if (tag.getTagid() != 0) { // if this tag has an id
			newTag = get(tag.getTagid()); // reassign if it exists, throw if not
		}


		if (program.containsTag(tag)) {
			// throw error if program already contains this tag
			throw new TagFoundException(tag.getTitle(), program);
		}

		// else, if this program doesn't already contain this tag
		if (tag.getTitle() != null) {
			newTag.setTitle(tag.getTitle());
		}
		if (tag.getHexcode() != null) {
			newTag.setHexcode(tag.getHexcode());
		}

		newTag = tagRepository.save(newTag);
		return newTag;
	}

	// OVERLOAD for convenience
	@Override
	public Tag save(
			Tag tag,
			long programid
	) {
		return save(programid, tag);
	}

	// OVERLOAD for convenience
	@Override
	public Tag replace(Tag tag) {
		save(tag.getTagid(), tag);
	}

	@Override
	public Tag replace(
			long tagid,
			Tag tag
	) {
		Tag oldTag = get(tagid); // throws if no such tag exists
		if (tag.getHexcode() != null) {
			oldTag.setHexcode(tag.getHexcode());
		}
		if (tag.getTitle() != null) {
			oldTag.setTitle(tag.getTitle());
		}
		return tagRepository.save(oldTag);
	}

	// OVERLOAD for convenience
	@Override
	public void replace(
			Tag tag,
			long tagid
	) {
		save(tagid, tag);
	}

	// OVERLOAD for convenience
	@Override
	public void update(Tag tag) {
		update(tag.getTagid(), tag);
	}

	// OVERLOAD for convenience
	@Override
	public void update(
			Tag tag,
			long tagid
	) {
		update(tagid, tag);
	}

	@Override
	public void update(
			long tagid,
			Tag tag
	) {
		Tag oldTag = get(tagid); // throws if no such tag
		if (tag.getTitle() != null) {
			oldTag.setTitle(tag.getTitle());
		}
		if (tag.getHexcode() != null) {
			oldTag.setHexcode(tag.getHexcode());
		}

		oldTag = tagRepository.save(oldTag);

		if (tag.getPrograms()
				    .size() > 0) {
			for (ProgramTags programTag : tag.getPrograms()) {
				Program program = programTag.getProgram();
				if (!program.containsTag(oldTag)) {
					program.addTag(oldTag);
				}
			}
		}

	}

	@Override
	public void delete(long tagid) {

	}

	@Override
	public void delete(Tag tag) {

	}

}
