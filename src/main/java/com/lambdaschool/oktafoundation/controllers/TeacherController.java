package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Student;
import com.lambdaschool.oktafoundation.models.Teacher;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.StudentRepository;
import com.lambdaschool.oktafoundation.repository.TeacherRepository;
import com.lambdaschool.oktafoundation.services.StudentService;
import com.lambdaschool.oktafoundation.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TeacherController
{
    @Autowired
    private TeacherRepository teacherrepos;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseRepository courserepos;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping(value = "/teachers", produces = "application/json")
    public ResponseEntity<?> getAllTeachers(){
        List<Teacher> allTeachers = new ArrayList<>();
        teacherService.findAll().iterator().forEachRemaining(allTeachers::add);

        return new ResponseEntity<>(allTeachers, HttpStatus.OK);
    }

    @GetMapping(value = "/teachers/{teachername}", produces = "application/json")
    public ResponseEntity<?> getTeacherByName(@PathVariable
                                                  String teachername){
        Teacher currTeacher = teacherrepos.findTeacherByTeachername(teachername);

        return new ResponseEntity<>(currTeacher, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping(value = "/teachers/{courseid}",
        consumes = {"application/json"},
        produces = "application/json")
    public ResponseEntity<?> addNewTeacherByCourseid(@PathVariable
                                               long courseid, @Valid @RequestBody Teacher newTeacher) throws URISyntaxException
    {
        newTeacher.setTeacherid(0);
        newTeacher = teacherService.update(newTeacher, courseid);


        return new ResponseEntity<>(newTeacher, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping(value = "/teachers/{courseid}/{teacherid}")
    public ResponseEntity<?> deleteCourseTeacher(@PathVariable long courseid,
                                                 @PathVariable long teacherid)
    {
        courserepos.findById(courseid).orElseThrow(() -> new ResourceNotFoundException("Oops! course with id " + courseid + " Not found!"));
        teacherrepos.deleteTeacherByTeacheridAndCourseid(teacherid, courseid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
