package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service(value = "studentTeacherService")
public class StudentTeacherServiceImpl
		implements StudentTeacherService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getAllStudents() {
		List<User> students = new ArrayList<>();

		userRepository.findAll()
				.iterator()
				.forEachRemaining(student -> {
					if (student.getRole() == RoleType.STUDENT) {
						students.add(student);
					}
				});

		return students;
	}

	@Override
	public List<User> getAllTeachers() {
		List<User> teachers = new ArrayList<>();

		userRepository.findAll()
				.iterator()
				.forEachRemaining(teacher -> {
					if (teacher.getRole() == RoleType.TEACHER) {
						teachers.add(teacher);
					}
				});

		return teachers;
	}

}
