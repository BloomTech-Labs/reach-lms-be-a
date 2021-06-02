package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
public interface CourseRepository
		extends CrudRepository<Course, Long> {

	Optional<Course> findByCourseName(String coursename);

	List<Course> findCoursesByProgram_ProgramId(long programid);

	List<Course> findByTag_tag_titleLikeIgnoreCase(String name);

	@Query(value = "SELECT * FROM user_courses uc\n" + "JOIN courses c ON uc.course_id = c.course_id " +
	               "WHERE user_id = :userId", nativeQuery = true)
	List<Course> findCoursesByUserId(long userId);

	@Query(value = "SELECT * FROM courses where course_id not in (SELECT course_id FROM user_courses WHERE " +
	               "user_id=:userId)", nativeQuery = true)
	List<Course> findAntiCoursesByUserId(long userId);

	/**
	 * Compares the given query against the {@link Course#getCourseName() coursename},
	 * {@link Course#getCourseDescription() coursedescription}, and {@link Course#getCourseCode() coursecode}.
	 * columns in the {@link Course courses} table.
	 * <p>
	 * Case (upper vs lower) will be ignored. If any column contains subtext that matches the query, that course
	 * will be considered a match and returned.
	 * <p>
	 * NOTE: 'ILIKE' is a Postgresql Operation. When running H2 locally, this query will throw an
	 * {@link java.sql.SQLFeatureNotSupportedException SQL Exception}.
	 * <p>
	 *
	 * @param query The search term that should be compared to the various fields mentioned above
	 *
	 * @return Any course that contains a match to the given search term
	 */
	@Query(
			value = "SELECT * FROM courses c WHERE CONCAT(c.course_name, ' ', c.course_description, ' ', c.course_code) ILIKE :query",
			nativeQuery = true)
	List<Course> search(String query);

	@Query(value = "WITH c as ( SELECT * FROM courses where course_id in (SELECT course_id FROM user_courses WHERE " +
	               "user_id=:userId) ) " +
	               "SELECT * FROM c WHERE CONCAT(c.course_name, ' ', c.course_description, ' ', c.course_code) ILIKE :query",
			nativeQuery = true)
	List<Course> search(
			long userId,
			String query
	);


}
