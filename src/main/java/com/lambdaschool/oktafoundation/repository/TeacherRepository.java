package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Teacher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TeacherRepository extends CrudRepository<Teacher, Long>
{
   Teacher findTeacherByTeachername(String username);

   @Modifying
   @Query(value = "DELETE FROM TEACHERCOURSES\n" +
       "WHERE TEACHERID = :teacherid AND COURSEID = :courseid\n", nativeQuery = true)
   void deleteTeacherByTeacheridAndCourseid(long teacherid, long courseid);
}
