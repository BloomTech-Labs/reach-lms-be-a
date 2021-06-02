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
import java.util.Optional;


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
	public Tag get(long tagId)
	throws TagNotFoundException {
		return find(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
	}

	@Override
	public Tag get(String title)
	throws TagNotFoundException {
		return find(title).orElseThrow(() -> new TagNotFoundException(title));
	}

	@Override
	public Optional<Tag> find(String title) {
		return tagRepository.findByTitle(title);
	}

	@Override
	public Optional<Tag> find(long tagId) {
		return tagRepository.findById(tagId);
	}

	@Override
	public List<Tag> getByProgram(long programId) {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findByPrograms_program_programId(programId)
				.iterator()
				.forEachRemaining(tags::add);
		return tags;
	}

	@Override
	public List<Tag> getByProgram(String programName) {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findByPrograms_program_programName(programName)
				.iterator()
				.forEachRemaining(tags::add);
		return tags;
	}

	@Override
	public Tag save(
			long programId,
			Tag tag
	)
	throws TagNotFoundException, TagFoundException, ProgramNotFoundException {
		Program program = programService.findProgramsById(programId); // throws if program does not exist
		Tag     newTag  = new Tag();
		if (tag.getTagId() != 0) { // if this tag has an id
			newTag = get(tag.getTagId()); // reassign if it exists, throw if not
		}
		if (program.containsTag(tag)) {
			// throw error if program already contains this tag
			throw new TagFoundException(tag.getTitle(), program);
		}
		// else, if this program doesn't already contain this tag
		if (tag.getTitle() != null) {
			newTag.setTitle(tag.getTitle());
		}
		if (tag.getHexCode() != null) {
			newTag.setHexCode(tag.getHexCode());
		}
		newTag = tagRepository.save(newTag);
		return newTag;
	}

	// OVERLOAD for convenience
	@Override
	public Tag save(
			Tag tag,
			long programId
	)
	throws TagNotFoundException, TagFoundException, ProgramNotFoundException {
		return save(programId, tag);
	}

	// OVERLOAD for convenience
	@Override
	public Tag replace(Tag tag) {
		return save(tag.getTagId(), tag);
	}

	@Override
	public Tag replace(
			long tagId,
			Tag tag
	) {
		Tag oldTag = get(tagId); // throws if no such tag exists
		if (tag.getHexCode() != null) {
			oldTag.setHexCode(tag.getHexCode());
		}
		if (tag.getTitle() != null) {
			oldTag.setTitle(tag.getTitle());
		}
		return tagRepository.save(oldTag);
	}

	// OVERLOAD for convenience
	@Override
	public Tag replace(
			Tag tag,
			long tagId
	) {
		return save(tagId, tag);
	}

	// OVERLOAD for convenience
	@Override
	public Tag update(Tag tag) {
		return update(tag.getTagId(), tag);
	}

	// OVERLOAD for convenience
	@Override
	public Tag update(
			Tag tag,
			long tagId
	) {
		return update(tagId, tag);
	}

	@Override
	public Tag update(
			long tagId,
			Tag tag
	) {
		Tag oldTag = get(tagId); // throws if no such tag
		if (tag.getTitle() != null) {
			oldTag.setTitle(tag.getTitle());
		}
		if (tag.getHexCode() != null) {
			oldTag.setHexCode(tag.getHexCode());
		}

		oldTag = tagRepository.save(oldTag);

		if (tag.getPrograms()
				    .size() > 0) {
			// only clear the programs attached to the old program
			// if someone passed in a tag with programs attached as a property
			oldTag.getPrograms()
					.clear();
			for (ProgramTags programTag : tag.getPrograms()) {
				// throws if no such program
				Program program = programService.findProgramsById(programTag.getProgram()
						.getProgramId());
				if (!program.containsTag(oldTag)) {
					program.addTag(oldTag);
				}
			}
		}

		return tagRepository.save(oldTag);

	}

	@Override
	public void delete(long tagId) {
		Tag toDelete = get(tagId); // throws if no such tag
		tagRepository.delete(toDelete);
	}

	@Override
	public void delete(Tag tag) {
		Tag toDelete = get(tag.getTagId()); // throws if no such tag
		tagRepository.delete(toDelete);
	}

	@Override
	public void deleteAll() {
		tagRepository.deleteAll();
	}

}
