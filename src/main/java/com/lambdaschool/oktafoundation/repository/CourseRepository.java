package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface CourseRepository
		extends CrudRepository<Course, Long> {

	List<Course> findCoursesByProgram_Programid(long programid);

	@Query(value = "SELECT * FROM USERCOURSES UC\n" + "JOIN COURSES C ON UC.COURSEID = C.COURSEID\n" +
	               "WHERE USERID = :userid", nativeQuery = true)
	List<Course> findCoursesByUserid(long userid);

	@Query(value = "SELECT * FROM courses where courseid not in (SELECT courseid FROM usercourses WHERE userid=:userid)",
			nativeQuery = true)
	List<Course> findAntiCoursesByUserId(long userid);

}
