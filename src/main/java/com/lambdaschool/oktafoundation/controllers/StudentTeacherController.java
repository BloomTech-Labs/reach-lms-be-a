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

	@GetMapping("/courses/course/{courseId}/enrolled")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllEnrolled(
			@PathVariable
					Long courseId
	) {

		List<EntityModel<User>> enrolledUsers = studentTeacherService.getCourseAttachedUsers(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledUsers,
				linkTo(methodOn(StudentTeacherController.class).getAllEnrolled(courseId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}


	@GetMapping("/courses/course/{courseId}/detached")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getAllNotEnrolled(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<User>> notEnrolledUsers = studentTeacherService.getCourseNotAttachedUsers(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(notEnrolledUsers,
				linkTo(methodOn(StudentTeacherController.class).getAllNotEnrolled(courseId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);

	}

	@GetMapping("/courses/course/{courseId}/enrolled-students")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getEnrolledStudents(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<User>> enrolledStudents = studentTeacherService.getCourseAttachedStudents(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledStudents,
				linkTo(methodOn(StudentTeacherController.class).getEnrolledStudents(courseId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseId}/enrolled-teachers")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getEnrolledTeachers(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<User>> enrolledTeachers = studentTeacherService.getCourseAttachedTeachers(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(enrolledTeachers,
				linkTo(methodOn(StudentTeacherController.class).getEnrolledTeachers(courseId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseId}/detached-students")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getDetachedStudents(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<User>> detachedStudents = studentTeacherService.getCourseDetachedStudents(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(detachedStudents,
				linkTo(methodOn(StudentTeacherController.class).getDetachedStudents(courseId)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/course/{courseId}/detached-teachers")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getDetachedTeachers(
			@PathVariable
					Long courseId
	) {
		List<EntityModel<User>> detachedTeachers = studentTeacherService.getCourseDetachedTeachers(courseId)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(detachedTeachers,
				linkTo(methodOn(StudentTeacherController.class).getDetachedTeachers(courseId)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PutMapping("/courses/course/{courseId}/user/{userId}")
	public ResponseEntity<?> attachUserToCourse(
			@PathVariable
					Long courseId,
			@PathVariable
					Long userId
	) {
		studentTeacherService.attachUserToCourse(userId, courseId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/courses/course/{courseId}/user/{userId}")
	public ResponseEntity<?> detachUserFromCourse(
			@PathVariable
					Long courseId,
			@PathVariable
					Long userId
	) {
		studentTeacherService.detachUserFromCourse(userId, courseId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/users/{userId}/replace-courses")
	public ResponseEntity<?> replaceUserCourses(
			@PathVariable
					Long userId,
			@RequestBody
					List<Long> courseIds
	) {
		User              updatedUser = studentTeacherService.replaceUserEnrollments(userId, courseIds);
		EntityModel<User> entityModel = userModelAssembler.toModel(updatedUser);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

}

