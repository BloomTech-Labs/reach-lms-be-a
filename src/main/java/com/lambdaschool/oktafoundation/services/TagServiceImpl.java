package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.TagNotFoundException;
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

	@Override
	public List<Tag> getAll() {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll()
				.iterator()
				.forEachRemaining(tags::add);
		return tags;
	}

	@Override
	public Tag getById(long tagid) {
		Tag tag = tagRepository.findById(tagid)
				.orElseThrow(() -> new TagNotFoundException(tagid));
		return tag;
	}

	@Override
	public List<Tag> getTagsByProgramId(long programid) {
		return null;
	}

}
