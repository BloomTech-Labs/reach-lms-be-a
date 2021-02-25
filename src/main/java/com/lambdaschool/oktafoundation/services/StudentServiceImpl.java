package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.CourseRepository;
import com.lambdaschool.oktafoundation.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service(value = "studentService")
public class StudentServiceImpl
    implements StudentService
{
    @Autowired
    private StudentRepository studentrepos;

    @Autowired
    private CourseRepository courserepos;

    @Override
    public List<Student> findAll()
    {
      List<Student> stdsList = new ArrayList<>();
      studentrepos.findAll().iterator()
          .forEachRemaining(stdsList::add);
      return stdsList;
    }

    @Override
    public Student update(Student student, long courseid)
    {
        Student currStudent = studentrepos.findStudentByStudentname(student.getStudentname());
        Course currCourse = courserepos.findById(courseid)
            .orElseThrow(() -> new ResourceNotFoundException("Course with id " + courseid + " Not found!"));


        currStudent.getCourses()
            .clear();


             currStudent.getCourses()
             .add(new StudentCourses(currStudent, currCourse));


        return studentrepos.save(currStudent);

    }
    /*
    @Override
    public void delete(string courseslong studentid)
    {
        studentrepos.findById(studentid) .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentid + "Not Found!"))
        studentrepos.deleteById(studentid);
    }

     */
}
