package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Student;
import com.lambdaschool.oktafoundation.models.Teacher;

import java.util.List;

public interface TeacherService
{
    List<Teacher> findAll();

    Teacher update(Teacher teacher, long courseid);
}
