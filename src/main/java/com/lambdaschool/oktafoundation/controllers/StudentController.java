package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Student;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.StudentRepository;
import com.lambdaschool.oktafoundation.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class StudentController {

	@Autowired
	private StudentRepository studentrepos;

	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseRepository courserepos;

	@GetMapping(value = "/students", produces = "application/json")
	public ResponseEntity<?> getAllStudents() {
		List<Student> allStudents = new ArrayList<>();
		studentService.findAll()
				.iterator()
				.forEachRemaining(allStudents::add);

		return new ResponseEntity<>(allStudents, HttpStatus.OK);
	}

	@GetMapping(value = "/students/{studentname}", produces = "application/json")
	public ResponseEntity<?> getStudentByName(
			@PathVariable
					String studentname
	) {
		Student currStudent = studentrepos.findStudentByStudentname(studentname);

		return new ResponseEntity<>(currStudent, HttpStatus.OK);
	}


	@PostMapping(value = "/students/{courseid}", consumes = {"application/json"}, produces = "application/json")
	public ResponseEntity<?> addNewStudent(
			@PathVariable
					long courseid,
			@Valid
			@RequestBody
					Student newStudent
	)
	throws URISyntaxException {
		newStudent.setStudentid(0);
		newStudent = studentService.save(newStudent, courseid);


		return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/students/{courseid}/{studentid}", produces = "application/json")
	public ResponseEntity<?> deleteCourseStudent(
			@PathVariable
					long courseid,
			@PathVariable
					long studentid
	) {
		List<Student> listStudents = new ArrayList<>();
		courserepos.findById(courseid)
				.orElseThrow(() -> new ResourceNotFoundException("Oops! course with id " + courseid + " Not found!"));
		studentrepos.deleteStudentByStudentidAndCourseid(studentid, courseid);
		studentrepos.findAll()
				.iterator()
				.forEachRemaining(listStudents::add);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
