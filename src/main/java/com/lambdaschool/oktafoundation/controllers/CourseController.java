package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
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

	@GetMapping("/courses/user/{userid}")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getUserCourses(
			@PathVariable
					long userid
	) {
		List<EntityModel<Course>> courses = courseService.findByUser(userid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getUserCourses(userid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/anti-user/{userid}")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getUserAntiCourses(
			@PathVariable
					long userid
	) {
		List<EntityModel<Course>> courses = courserepos.findAntiCoursesByUserId(userid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getUserAntiCourses(userid)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/courses/mappify-by-user/{userid}")
	public ResponseEntity<?> getMappifiedCoursesByUser(
			@PathVariable
					long userid
	) {
		List<Course> enrolledCourses = new ArrayList<>();
		courserepos.findCoursesByUserid(userid)
				.iterator()
				.forEachRemaining(enrolledCourses::add);
		List<Course> availableCourses = new ArrayList<>();
		courserepos.findAntiCoursesByUserId(userid)
				.iterator()
				.forEachRemaining(availableCourses::add);
		Map<String, List<Course>> mappedCourses = new HashMap<>();
		mappedCourses.put("enrolled", enrolledCourses);
		mappedCourses.put("available", availableCourses);
		return new ResponseEntity<>(mappedCourses, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/student/{userid}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getStudentCourses(
			@PathVariable
					long userid
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByUserid(userid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getStudentCourses(userid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/teacher/{userid}", produces = "application/json")
	public ResponseEntity<?> getTeacherCourses(
			@PathVariable
					long userid
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByUserid(userid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getTeacherCourses(userid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/course/{courseid}", produces = {"application/json"})
	public ResponseEntity<EntityModel<Course>> getCourseByCourseId(
			@PathVariable
					long courseid
	) {
		EntityModel<Course> course = courseModelAssembler.toModel(courseService.findCourseById(courseid));
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/{programid}", produces = {"application/json"})
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getCoursesByProgramid(
			@PathVariable
					long programid
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByProgram_Programid(programid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getCoursesByProgramid(programid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PostMapping(value = "/courses/{programid}/course", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> addNewCourse(
			@PathVariable
					long programid,
			@Valid
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseid(0);
		newCourse = courseService.save(programid, newCourse);
		// location header for the newly created course
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newCourseURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{courseid}")
				.buildAndExpand(newCourse.getCourseid())
				.toUri();
		responseHeaders.setLocation(newCourseURI);
		return new ResponseEntity<>(newCourse, responseHeaders, HttpStatus.CREATED);
	}

	@PatchMapping(value = "/courses/{courseid}", consumes = "application/json")
	public ResponseEntity<?> updateCourse(
			@PathVariable
					long courseid,
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseid(courseid);
		courseService.update(courseid, newCourse);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(value = "/courses/{courseid}", consumes = "application/json")
	public ResponseEntity<?> updateFullCourse(
			@PathVariable
					long courseid,
			@RequestBody
					Course newCourse
	) {
		newCourse.setCourseid(courseid);
		courseService.update(courseid, newCourse);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/courses/{courseid}")
	public ResponseEntity<?> deleteCourseById(
			@PathVariable
					long courseid
	) {
		courserepos.findById(courseid)
				.orElseThrow(() -> new ResourceNotFoundException("Course with id " + courseid + " Not Found!"));
		courseService.delete(courseid);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


}
