package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseRepository
    extends CrudRepository<Course, Long>
{
    List<Course> findCoursesByProgram_Programid(long programid);

    @Query(value = "SELECT * FROM TEACHERCOURSES SC\n" +
        "JOIN COURSES C ON SC.COURSEID = C.COURSEID\n" +
        "WHERE TEACHERID = :teacherid", nativeQuery = true)
    List<Course> findCoursesByTeacherid(long teacherid);

    @Query(value = "SELECT * FROM STUDENTCOURSES SC\n" +
        "JOIN COURSES C ON SC.COURSEID = C.COURSEID\n" +
        "WHERE STUDENTID = :studentid", nativeQuery = true)
    List<Course> findCoursesByStudentid(long studentid);
}
