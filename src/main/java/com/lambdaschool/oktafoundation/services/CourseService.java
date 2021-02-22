package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Course;

import java.util.List;

public interface CourseService
{
    List<Course> findAll();

    Course findCourseById(long courseid);

    Course save(long courseid, Course course);

    Course update(long courseid, Course course);

    void delete(long courseid);



}
