package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Student;

import java.util.List;

public interface StudentService
{
    List<Student> findAll();

    Student update(Student student, long courseid);

    /*
    Student deleteBy(long studentid);
    */
}
