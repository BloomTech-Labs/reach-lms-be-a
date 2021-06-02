package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.modelAssemblers.ProgramModelAssembler;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.ProgramIn;
import com.lambdaschool.oktafoundation.models.Tag;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.repository.TagRepository;
import com.lambdaschool.oktafoundation.services.ProgramService;
import com.lambdaschool.oktafoundation.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class ProgramController {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private ProgramService programService;

	@Autowired
	private ProgramRepository programRepos;

	@Autowired
	private ProgramModelAssembler programModelAssembler;

	@Autowired
	private TagService tagService;

	@GetMapping(value = "/programs/tag-title/{tagTitle}")
	public ResponseEntity<?> listProgramsByTag(
			@PathVariable
					String tagTitle
	) {
		List<Program> programs = programService.findProgramsByTagName(tagTitle);
		return new ResponseEntity<>(programs, HttpStatus.OK);
	}

	@GetMapping(value = "/programs/program/{programId}/tags")
	public ResponseEntity<?> listTagsByProgramId(
			@PathVariable
					Long programId
	) {
		List<Tag> tags = tagService.getByProgram(programId);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping(value = "/programs", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Program>>> listAllPrograms() {
		List<EntityModel<Program>> programEntities = programService.findAll()
				.stream()
				.map(programModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Program>> collectionModel = CollectionModel.of(programEntities,
				// Link to SELF -- `listAllPrograms` method
				linkTo(methodOn(ProgramController.class).listAllPrograms()).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/programs/program/{programId}", produces = "application/json")
	public ResponseEntity<EntityModel<Program>> getProgramById(
			@PathVariable
					Long programId
	) {
		EntityModel<Program> entityProgram = programModelAssembler.toModel(programService.findProgramsById(programId));
		return new ResponseEntity<>(entityProgram, HttpStatus.OK);
	}

	@GetMapping(value = "/programs/program/name/{programName}", produces = "application/json")
	public ResponseEntity<EntityModel<Program>> getProgramByName(
			@PathVariable
					String programName
	) {
		EntityModel<Program> entityProgram = programModelAssembler.toModel(programService.findProgramsByName(programName));
		return new ResponseEntity<>(entityProgram, HttpStatus.OK);
	}


	@GetMapping(value = "/programs/{userId}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Program>>> getProgramsByUserId(
			@PathVariable
					long userId
	) {
		List<EntityModel<Program>> programEntities = programRepos.findProgramsByUserId(userId)
				.stream()
				.map(programModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Program>> collectionModel = CollectionModel.of(programEntities,
				linkTo(methodOn(ProgramController.class).getProgramsByUserId(userId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PostMapping(value = "/programs/{userId}/program", consumes = {"application/json"}, produces = "application/json")
	public ResponseEntity<?> addNewProgram(
			@PathVariable
					long userId,
			@Valid
			@RequestBody
					Program newProgram
	) {
		newProgram.setProgramId(0);
		newProgram = programService.save(userId, newProgram);
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newProgramURI = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{programId}")
				.buildAndExpand(newProgram.getProgramId())
				.toUri();
		responseHeaders.setLocation(newProgramURI);
		return new ResponseEntity<>(newProgram, responseHeaders, HttpStatus.CREATED);
	}

	@PostMapping(value = "/programs/{userId}/program-in", consumes = {"application/json"}, produces = "application/json")
	public ResponseEntity<?> addNewProgram(
			@PathVariable
					long userId,
			@Valid
			@RequestBody
					ProgramIn program
	) {
		program.setProgramid(0);
		Program newProgram = programService.save(userId, program);
		return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
	}

	@PutMapping(value = "/programs/program/{programId}", consumes = "application/json")
	public ResponseEntity<?> editEntireProgram(
			@Valid
			@RequestBody
					Program editProgram,
			@PathVariable
					Long programId
	) {
		editProgram.setProgramId(programId);
		Program newProgram = programService.update(editProgram, programId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping(value = "/programs/program/{programId}", consumes = "application/json")
	public ResponseEntity<?> editPartialProgram(
			@RequestBody
					Program editPartialProgram,
			@PathVariable
					Long programId
	) {
		programService.update(editPartialProgram, programId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/programs/program-in/{programId}")
	public ResponseEntity<?> editPartialProgram(
			@RequestBody
					ProgramIn programIn,
			@PathVariable
					Long programId
	) {
		programIn.setProgramid(programId);
		Program saved = programService.update(programIn, programId);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}

	@DeleteMapping(value = "/programs/program/{programId}")
	public ResponseEntity<?> deleteProgram(
			@PathVariable
					long programId
	) {
		programService.delete(programId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
