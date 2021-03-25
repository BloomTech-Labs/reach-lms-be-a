package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Program;


public interface RolesService {

	boolean ensureAdmin();
	boolean ensureTeacher();
	boolean ensureStudent();
	boolean ensureAdminOrTeacher();
	boolean canWriteProgram(Program program);
	boolean canWriteCourse(Course course);

}
