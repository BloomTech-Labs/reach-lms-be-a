package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Student;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional   //Required since we'll modify the studentcourses table
public interface StudentRepository extends CrudRepository<Student, Long>
{
     Student findStudentByStudentname(String username);

     @Modifying      // to mark it's a delete query, same can be done on update but no need
     @Query(value = "DELETE FROM STUDENTCOURSES p\n" +
         "WHERE STUDENTID = :studentid AND COURSEID = :courseid\n", nativeQuery = true)
     void deleteStudentByStudentidAndCourseid(long studentid, long courseid);
}
