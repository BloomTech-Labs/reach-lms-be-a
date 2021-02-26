package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher, Long>
{
   Teacher findTeacherByTeachername(String username);
}
