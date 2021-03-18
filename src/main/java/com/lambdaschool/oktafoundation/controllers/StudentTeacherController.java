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

	@GetMapping("/students/students")
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

	@GetMapping("/teachers/teachers")
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

}
