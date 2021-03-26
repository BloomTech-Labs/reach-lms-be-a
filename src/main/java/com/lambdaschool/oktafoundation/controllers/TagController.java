package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.models.Tag;
import com.lambdaschool.oktafoundation.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class TagController {

	@Autowired
	TagRepository tagRepository;

	@GetMapping(value = "/tags")
	public ResponseEntity<?> getAllTags() {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll()
				.iterator()
				.forEachRemaining(tags::add);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping(value = "/tags/program/{programid}")
	public ResponseEntity<?> getTagsByProgram(
			@PathVariable
					Long programid
	) {
		List<Tag> tags = new ArrayList<>();
		tagRepository.findByPrograms_program_programid(programid)
				.iterator()
				.forEachRemaining(tags::add);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

}
