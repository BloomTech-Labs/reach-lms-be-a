package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.modelAssemblers.ProgramModelAssembler;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.services.ProgramService;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class ProgramController {

	@Autowired
	private ProgramService programService;

	@Autowired
	private ProgramRepository programRepos;

	@Autowired
	private ProgramModelAssembler programModelAssembler;

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


	@GetMapping(value = "/programs/{userid}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Program>>> getProgramsByUserId(
			@PathVariable
					long userid
	) {
		List<EntityModel<Program>> programEntities = programRepos.findProgramsByUserid(userid)
				.stream()
				.map(programModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Program>> collectionModel = CollectionModel.of(programEntities,
				linkTo(methodOn(ProgramController.class).getProgramsByUserId(userid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PostMapping(value = "/programs/{userid}/program", consumes = {"application/json"}, produces = "application/json")
	public ResponseEntity<?> addNewProgram(
			@PathVariable
					long userid,
			@Valid
			@RequestBody
					Program newProgram
	)
	throws URISyntaxException {
		// TODO: check calling user's role permissions before we allow a POST
		newProgram.setProgramid(0);
		newProgram = programService.save(userid, newProgram);

		HttpHeaders responseHeaders = new HttpHeaders();
		URI newProgramURI = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{programid}")
				.buildAndExpand(newProgram.getProgramid())
				.toUri();
		responseHeaders.setLocation(newProgramURI);
		// TODO: send `newProgramURI` as part of the body

		return new ResponseEntity<>(newProgram, responseHeaders, HttpStatus.CREATED);
	}

	@PutMapping(value = "/programs/program/{programid}", consumes = "application/json")
	public ResponseEntity<?> editEntireProgram(
			@Valid
			@RequestBody
					Program editProgram,
			@PathVariable
					Long programid
	) {
		// TODO: check calling user's role permissions before we allow a PUT
		editProgram.setProgramid(programid);
		Program newtProgram = programService.update(editProgram, programid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping(value = "/programs/program/{programid}", consumes = "application/json")
	public ResponseEntity<?> editPartialProgram(
			@RequestBody
					Program editPartialProgram,
			@PathVariable
					Long programid
	) {
		// TODO: check calling user's role permissions before we allow a PATCH
		programService.update(editPartialProgram, programid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/programs/program/{programid}")
	public ResponseEntity<?> deleteProgram(
			@PathVariable
					long programid
	) {
		// TODO: check calling user's role permissions before we allow a DELETE
		programRepos.findById(programid)
				.orElseThrow(() -> new ResourceNotFoundException("Program id with id" + programid + " Not found!"));
		programRepos.deleteById(programid);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
