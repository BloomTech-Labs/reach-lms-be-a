package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository
    extends CrudRepository<Course, Long>
{
    List<Course> findCoursesByProgram_Programid(long programid);

}
