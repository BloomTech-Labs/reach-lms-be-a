package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Role;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserRoles;
import com.lambdaschool.oktafoundation.utils.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class CsvServiceImpl
		implements CsvService {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Override
	public void save(MultipartFile file) {
		Role studentRole = roleService.findByName(RoleType.STUDENT.name());
		try {
			List<User> users = CsvHelper.csvToStudents(file);
			for (User user : users) {
				System.out.println("USER TO SAVE -- " + user);
				user.getRoles().add(new UserRoles(user, studentRole));
				userService.save(user);
			}
		} catch (IOException e) {
			throw new RuntimeException("failure to store csv data: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
