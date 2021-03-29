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

	List<Course> findByTags_tag_titleLikeIgnoreCase(String name);

	List<Course> findByTags_tag_tagid(long tagid);

	@Query(value = "SELECT * FROM usercourses uc\n" + "JOIN courses c ON uc.courseid = c.courseid " +
	               "WHERE userid = :userid", nativeQuery = true)
	List<Course> findCoursesByUserid(long userid);

	@Query(value = "SELECT * FROM courses where courseid not in (SELECT courseid FROM usercourses WHERE userid=:userid)",
			nativeQuery = true)
	List<Course> findAntiCoursesByUserId(long userid);

	/**
	 * Compares the given {@link CourseRepository#search(String) query} against the
	 * {@link Course#getCoursename() coursename},
	 * {@link Course#getCoursedescription() coursedescription}, and {@link Course#getCoursecode() coursecode}.
	 * columns in the {@link Course courses} table.
	 * <p>
	 * Case (upper vs lower) will be ignored. If any column contains subtext that matches the query, that course
	 * will be considered a match and returned.
	 * <p>
	 * NOTE: 'ILIKE' is a Postgresql Operation. When running H2 locally, this query will throw an
	 * {@link java.sql.SQLFeatureNotSupportedException SQL Exception}.
	 * <p>
	 * @param query The search term that should be compared to the various fields mentioned above
	 *
	 * @return Any course that contains a match to the given search term
	 */
	@Query(value = "SELECT * FROM courses c WHERE CONCAT(c.coursename, ' ', c.coursedescription, ' ', c.coursecode) " +
	               "ILIKE %?1%", nativeQuery = true)
	List<Course> search(String query);


}
