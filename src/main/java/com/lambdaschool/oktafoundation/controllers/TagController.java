package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.models.Tag;
import com.lambdaschool.oktafoundation.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping(value = "/tags")
	public ResponseEntity<?> getAll() {
		List<Tag> tags = tagService.getAll();
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping(value = "/tags/tag/{tagid}")
	public ResponseEntity<?> get(
			@PathVariable
					Long tagid
	) {
		Tag tag = tagService.get(tagid);
		return new ResponseEntity<>(tag, HttpStatus.OK);
	}

	@GetMapping(value = "/tags/program/{programid}")
	public ResponseEntity<?> getByProgram(
			@PathVariable
					Long programid
	) {
		List<Tag> tags = tagService.getByProgram(programid);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping(value = "/tags/program/name/{programname}")
	public ResponseEntity<?> getByProgram(
			@PathVariable
					String programname
	) {
		List<Tag> tags = tagService.getByProgram(programname);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@PostMapping(value = "/tags/new-by-program/{programid}")
	public ResponseEntity<?> post(
			@PathVariable
					Long programid,
			@Valid
			@RequestBody
					Tag tag
	) {
		tagService.save(tag, programid);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

//	@PatchMapping(value="/tags/tag/{tagid}")


	@DeleteMapping("/tags/tag/{tagid}")
	public ResponseEntity<?> delete(
			@PathVariable
					Long tagid
	) {
		return new ResponseEntity(null, null, HttpStatus.OK);
	}

}
