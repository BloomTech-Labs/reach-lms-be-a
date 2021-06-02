package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.modelAssemblers.ModuleModelAssembler;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.repository.ModuleRepository;
import com.lambdaschool.oktafoundation.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class ModuleController {

	@Autowired
	ModuleRepository modulerepos;

	@Autowired
	ModuleService moduleService;

	@Autowired
	ModuleModelAssembler moduleModelAssembler;

	@GetMapping(value = "/modules", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Module>>> getAllModules() {
		List<EntityModel<Module>> modules = moduleService.findAll()
				.stream()
				.map(moduleModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Module>> entityModules = CollectionModel.of(modules,
				linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel()
		);


		return new ResponseEntity<>(entityModules, HttpStatus.OK);
	}

	@GetMapping("/modules/markdown/{moduleId}")
	public ResponseEntity<?> getMarkdownByModuleId(
			@PathVariable
					Long moduleId
	) {
		Module module = moduleService.findModulesById(moduleId);
		return new ResponseEntity<>(module.getModuleContent(), HttpStatus.OK);
	}

	@PutMapping("/modules/markdown/{moduleId}")
	public ResponseEntity<?> replaceMarkdownByModuleId(@PathVariable Long moduleId, @RequestBody String markdown) {
		moduleService.replaceMarkdown(moduleId, markdown);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/modules/module/{moduleId}", produces = "application/json")
	public ResponseEntity<EntityModel<Module>> getModuleById(
			@PathVariable
					Long moduleId
	) {
		Module module = moduleService.findModulesById(moduleId);
		return new ResponseEntity<>(moduleModelAssembler.toModel(module), HttpStatus.OK);
	}

	@GetMapping(value = "/modules/{courseId}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Module>>> getModulesByCourseId(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<Module>> allModules = modulerepos.findModulesByCourse_CourseId(courseId)
				.stream()
				.map(moduleModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Module>> entityModules = CollectionModel.of(allModules,
				linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel()
		);

		return new ResponseEntity<>(entityModules, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'ROLE_ADMIN', 'ROLE_TEACHER')")
	@PostMapping(value = "/modules/{courseId}/module", produces = "application/json")
	public ResponseEntity<?> addNewModule(
			@PathVariable
					long courseId,
			@Valid
			@RequestBody
					Module newModule
	)
	throws URISyntaxException {
		newModule.setModuleId(0);
		newModule = moduleService.save(courseId, newModule);

		HttpHeaders responseHeaders = new HttpHeaders();
		URI newModuleURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{moduleId}")
				.buildAndExpand(newModule.getModuleId())
				.toUri();
		responseHeaders.setLocation(newModuleURI);

		return new ResponseEntity<>(newModule, responseHeaders, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'ROLE_ADMIN', 'ROLE_TEACHER')")
	@PatchMapping(value = "/modules/{moduleId}", consumes = "application/json")
	public ResponseEntity<?> updateModule(
			@PathVariable
					long moduleId,
			@RequestBody
					Module newModule
	) {
		newModule.setModuleId(moduleId);
		moduleService.update(moduleId, newModule);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'ROLE_ADMIN', 'ROLE_TEACHER')")
	@PutMapping(value = "/modules/{moduleId}", consumes = "application/json")
	public ResponseEntity<?> updateFullModule(
			@PathVariable
					long moduleId,
			@RequestBody
					Module newModule
	) {
		newModule.setModuleId(moduleId);
		moduleService.update(moduleId, newModule);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'ROLE_ADMIN', 'ROLE_TEACHER')")
	@DeleteMapping(value = "/modules/{moduleId}")
	public ResponseEntity<?> deleteModuleById(
			@PathVariable
					long moduleId
	) {
		modulerepos.findById(moduleId)
				.orElseThrow(() -> new ResourceNotFoundException("Module with id" + moduleId + " not found."));
		moduleService.delete(moduleId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
