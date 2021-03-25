package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.modelAssemblers.UserModelAssembler;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.services.StudentTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/users/admins")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllAdmins() {
		List<EntityModel<User>> adminEntities = studentTeacherService.getAllAdmins()
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(adminEntities,
				linkTo(methodOn(StudentTeacherController.class).getAllAdmins()).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

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


	@GetMapping("/courses/course/{courseid}/detached")
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

	@GetMapping("/courses/course/{courseid}/enrolled-students")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getEnrolledStudents(
			@PathVariable
					Long courseid
	) {
		List<EntityModel<User>> enrolledStudents = studentTeacherService.getCourseAttachedStudents(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledStudents,
				linkTo(methodOn(StudentTeacherController.class).getEnrolledStudents(courseid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseid}/enrolled-teachers")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getEnrolledTeachers(
			@PathVariable
					Long courseid
	) {
		List<EntityModel<User>> enrolledTeachers = studentTeacherService.getCourseAttachedTeachers(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledTeachers,
				linkTo(methodOn(StudentTeacherController.class).getEnrolledTeachers(courseid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseid}/detached-students")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getDetachedStudents(
			@PathVariable
					Long courseid
	) {
		List<EntityModel<User>> detachedStudents = studentTeacherService.getCourseDetachedStudents(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(detachedStudents,
				linkTo(methodOn(StudentTeacherController.class).getDetachedStudents(courseid)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseid}/detached-teachers")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getDetachedTeachers(
			@PathVariable
					Long courseid
	) {
		List<EntityModel<User>> detachedTeachers = studentTeacherService.getCourseDetachedTeachers(courseid)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(detachedTeachers,
				linkTo(methodOn(StudentTeacherController.class).getDetachedTeachers(courseid)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PutMapping("/courses/course/{courseid}/user/{userid}")
	public ResponseEntity<?> attachUserToCourse(
			@PathVariable
					Long courseid,
			@PathVariable
					Long userid
	) {
		studentTeacherService.attachUserToCourse(userid, courseid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/courses/course/{courseid}/user/{userid}")
	public ResponseEntity<?> detachUserFromCourse(
			@PathVariable
					Long courseid,
			@PathVariable
					Long userid
	) {
		studentTeacherService.detachUserFromCourse(userid, courseid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/users/{userid}/replace-courses")
	public ResponseEntity<?> replaceUserCourses(
			@PathVariable
					Long userid,
			@RequestBody
					List<Long> courseids
	) {
		User              updatedUser = studentTeacherService.replaceUserEnrollments(userid, courseids);
		EntityModel<User> entityModel = userModelAssembler.toModel(updatedUser);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

}

