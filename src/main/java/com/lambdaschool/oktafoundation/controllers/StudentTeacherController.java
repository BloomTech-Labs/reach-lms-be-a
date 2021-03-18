package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.modelAssemblers.UserModelAssembler;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.services.StudentTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class StudentTeacherController {

	@Autowired
	StudentTeacherService studentTeacherService;

	@Autowired
	UserModelAssembler userModelAssembler;

	@GetMapping("/users/students")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllStudents() {
		List<EntityModel<User>> studentEntities = studentTeacherService.getAllStudents()
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(studentEntities,
				linkTo(methodOn(StudentTeacherController.class).getAllStudents()).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/users/teachers")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllTeachers() {
		List<EntityModel<User>> teacherEntities = studentTeacherService.getAllTeachers()
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(teacherEntities,
				linkTo(methodOn(StudentTeacherController.class).getAllTeachers()).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseid}/enrolled")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllEnrolled(
			@PathVariable
					Long courseid
	) {

		List<EntityModel<User>> enrolledUsers = studentTeacherService.getCourseAttachedUsers(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledUsers,
				linkTo(methodOn(StudentTeacherController.class).getAllEnrolled(courseid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseid}/un-enrolled")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllNotEnrolled(
			@PathVariable
					Long courseid
	) {
		List<EntityModel<User>> notEnrolledUsers = studentTeacherService.getCourseNotAttachedUsers(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(notEnrolledUsers,
				linkTo(methodOn(StudentTeacherController.class).getAllNotEnrolled(courseid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);

	}

}
