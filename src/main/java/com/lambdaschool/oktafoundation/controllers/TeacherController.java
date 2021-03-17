package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.modelAssemblers.TeacherModelAssembler;
import com.lambdaschool.oktafoundation.models.Teacher;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.TeacherRepository;
import com.lambdaschool.oktafoundation.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class TeacherController {

	@Autowired
	private TeacherRepository teacherrepos;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private CourseRepository courserepos;

	@Autowired
	private TeacherModelAssembler teacherModelAssembler;

	@GetMapping(value = "/teachers", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Teacher>>> getAllTeachers() {
		List<EntityModel<Teacher>> teachers = teacherService.findAll()
				.stream()
				.map(teacherModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Teacher>> collectionModel = CollectionModel.of(teachers,
				linkTo(methodOn(TeacherController.class).getAllTeachers()).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/teachers/{teachername}", produces = "application/json")
	public ResponseEntity<EntityModel<Teacher>> getTeacherByName(
			@PathVariable
					String teachername
	) {
		EntityModel<Teacher> teacher = teacherModelAssembler.toModel(teacherrepos.findTeacherByTeachername(teachername));

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	@PostMapping(value = "/teachers/{courseid}", consumes = {"application/json"}, produces = "application/json")
	public ResponseEntity<?> addNewTeacherByCourseid(
			@PathVariable
					long courseid,
			@Valid
			@RequestBody
					Teacher newTeacher
	)
	throws URISyntaxException {
		newTeacher.setTeacherid(0);
		newTeacher = teacherService.update(newTeacher, courseid);


		return new ResponseEntity<>(newTeacher, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	@DeleteMapping(value = "/teachers/{courseid}/{teacherid}")
	public ResponseEntity<?> deleteCourseTeacher(
			@PathVariable
					long courseid,
			@PathVariable
					long teacherid
	) {
		List<Teacher> teacherList = new ArrayList<>();
		courserepos.findById(courseid)
				.orElseThrow(() -> new ResourceNotFoundException("Oops! course with id " + courseid + " Not found!"));
		teacherrepos.deleteTeacherByTeacheridAndCourseid(teacherid, courseid);

		teacherrepos.findAll()
				.iterator()
				.forEachRemaining(teacherList::add);

		return new ResponseEntity<>(teacherList, HttpStatus.OK);
	}

}
