package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.User;

import java.util.List;


public interface StudentTeacherService {

	List<User> getAllStudents();
	List<User> getAllTeachers();

}
