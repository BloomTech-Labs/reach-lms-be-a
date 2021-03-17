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
import java.util.List;
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

	@GetMapping(value = "/courses/student/{studentid}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Course>>> getStudentCourses(
			@PathVariable
					long studentid
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByStudentid(studentid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getStudentCourses(studentid)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/teacher/{teacherid}", produces = "application/json")
	public ResponseEntity<?> getTeacherCourses(
			@PathVariable
					long teacherid
	) {
		List<EntityModel<Course>> courses = courserepos.findCoursesByTeacherid(teacherid)
				.stream()
				.map(courseModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Course>> collectionModel = CollectionModel.of(courses,
				linkTo(methodOn(CourseController.class).getTeacherCourses(teacherid)).withSelfRel()
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
