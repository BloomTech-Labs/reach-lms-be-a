package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Student;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.StudentRepository;
import com.lambdaschool.oktafoundation.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController
{
    @Autowired
    CourseRepository courserepos;

    @Autowired
    StudentRepository studentrepos;

    @Autowired
    CourseService courseService;

    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> getAllCourses()
    {

       List<Course> courses = courseService.findAll();

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


    @GetMapping(value = "/courses/student/{studentid}", produces = "application/json")
    public ResponseEntity<?> getStudentCourses(@PathVariable long studentid){
        List<Course> courses = courserepos.findCoursesByStudentid(studentid);

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/courses/teacher/{teacherid}", produces = "application/json")
    public ResponseEntity<?> getTeacherCourses(@PathVariable long teacherid){
        List<Course> courses = courserepos.findCoursesByTeacherid(teacherid);

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }



    @GetMapping(value = "/courses/course/{courseid}", produces = {"application/json"})
    public ResponseEntity<?> getCourseByCourseId(@PathVariable  long courseid)
    {
       Course course = courseService.findCourseById(courseid);

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping(value = "/courses/{programid}", produces = {"application/json"})
    public ResponseEntity<?> getCoursesByProgramid(@PathVariable long programid)
    {
        List<Course> allCourses = new ArrayList<>();
        courserepos.findCoursesByProgram_Programid(programid)
            .iterator().forEachRemaining(allCourses::add);

        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

 //   @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping(value = "/courses/{programid}/course", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> addNewCourse(@PathVariable long programid, @Valid @RequestBody Course newCourse) throws URISyntaxException
    {
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
   // @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PatchMapping(value = "/courses/{courseid}", consumes = "application/json")
    public ResponseEntity<?> updateCourse(@PathVariable long courseid,
                                          @RequestBody Course newCourse)
    {
        newCourse.setCourseid(courseid);
        courseService.update(courseid, newCourse);

        return new ResponseEntity<>(HttpStatus.OK);
    }
  //  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping(value = "/courses/{courseid}", consumes = "application/json")
    public ResponseEntity<?> updateFullCourse(@PathVariable long courseid,
                                          @RequestBody Course newCourse)
    {
        newCourse.setCourseid(courseid);
        courseService.update(courseid, newCourse);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping(value = "/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(@PathVariable long courseid)
    {
        courserepos.findById(courseid)
            .orElseThrow(() -> new ResourceNotFoundException("Course with id " + courseid + " Not Found!"));
        courseService.delete(courseid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
