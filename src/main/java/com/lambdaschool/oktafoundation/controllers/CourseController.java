package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.CourseNotFoundException;
import com.lambdaschool.oktafoundation.modelAssemblers.CourseModelAssembler;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.services.CourseService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class CourseController {

	@Autowired
	CourseRepository courserepos;

	@Autowired
	CourseService courseService;

	@Autowired
	CourseModelAssembler courseModelAssembler;

	@GetMapping(value = "/courses", produces = {"application/json"})
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getAllCourses() {

		List<EntityModel<Course>> courses = courseService.findAll()
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> entityCourses = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getAllCourses()).withSelfRel()
		);

		return new ResponseEntity<>(entityCourses, HttpStatus.OK);
	}

	@GetMapping("/courses/relevant")
	public ResponseEntity<?> getRelevantCourses(
			@RequestParam(required = false)
					String query
	) {
		List<EntityModel<Course>> courses = courseService.findRelevant(query)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getRelevantCourses(query)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/by-tag/{tagTitle}")
	public ResponseEntity<?> getCoursesByTag(
			@PathVariable
					String tagTitle
	) {
		List<Course> courses = courseService.findByTag(tagTitle);
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	@GetMapping("/courses/user/{userId}")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getUserCourses(
			@PathVariable
					long userId
	) {
		List<EntityModel<Course>> courses = courseService.findByUser(userId)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getUserCourses(userId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/anti-user/{userId}")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getUserAntiCourses(
			@PathVariable
					long userId
	) {
		List<EntityModel<Course>> courses = courserepos.findAntiCoursesByUserId(userId)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getUserAntiCourses(userId)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/mappify-by-user/{userId}")
	public ResponseEntity<?> getMappifiedCoursesByUser(
			@PathVariable
					long userId
	) {
		List<Course> enrolledCourses = new ArrayList<>();
		courserepos.findCoursesByUserId(userId)
				.iterator()
				.forEachRemaining(enrolledCourses::add);
		List<Course> availableCourses = new ArrayList<>();
		courserepos.findAntiCoursesByUserId(userId)
				.iterator()
				.forEachRemaining(availableCourses::add);
		Map<String, List<Course>> mappedCourses = new HashMap<>();
		mappedCourses.put("enrolled", enrolledCourses);
		mappedCourses.put("available", availableCourses);
		return new ResponseEntity<>(mappedCourses, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/student/{userId}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getStudentCourses(
			@PathVariable
					long userId
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByUserId(userId)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getStudentCourses(userId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/teacher/{userId}", produces = "application/json")
	public ResponseEntity<?> getTeacherCourses(
			@PathVariable
					long userId
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByUserId(userId)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getTeacherCourses(userId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/course/{courseId}", produces = {"application/json"})
	public ResponseEntity<EntityModel<Course>> getCourseByCourseId(
			@PathVariable
					long courseId
	) {
		EntityModel<Course> course = courseModelAssembler.toModel(courseService.findCourseById(courseId));
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/{programId}", produces = {"application/json"})
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getCoursesByProgramid(
			@PathVariable
					long programId
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByProgram_ProgramId(programId)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getCoursesByProgramid(programId)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PostMapping(value = "/courses/{programId}/course", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> addNewCourse(
			@PathVariable
					long programId,
			@Valid
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseId(0);
		newCourse = courseService.save(programId, newCourse);
		// location header for the newly created course
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newCourseURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{courseId}")
				.buildAndExpand(newCourse.getCourseId())
				.toUri();
		responseHeaders.setLocation(newCourseURI);
		return new ResponseEntity<>(newCourse, responseHeaders, HttpStatus.CREATED);
	}

	@PatchMapping(value = "/courses/{courseId}", consumes = "application/json")
	public ResponseEntity<?> updateCourse(
			@PathVariable
					long courseId,
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseId(courseId);
		courseService.update(courseId, newCourse);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(value = "/courses/{courseId}", consumes = "application/json")
	public ResponseEntity<?> updateFullCourse(
			@PathVariable
					long courseId,
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseId(courseId);
		courseService.update(courseId, newCourse);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/courses/{courseId}")
	public ResponseEntity<?> deleteCourseById(
			@PathVariable
					long courseId
	) {
		courserepos.findById(courseId)
				.orElseThrow(() -> new CourseNotFoundException(courseId));
		courseService.delete(courseId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


}
