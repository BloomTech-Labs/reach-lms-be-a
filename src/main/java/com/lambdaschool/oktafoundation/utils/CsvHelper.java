package com.lambdaschool.oktafoundation.utils;


import com.lambdaschool.oktafoundation.models.Role;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserRoles;
import com.lambdaschool.oktafoundation.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvHelper {
	public static String CSV_TYPE = "text/csv";
	static String[] USER_HEADERS = {"email", "firstname", "lastname", "phonenumber", "role"};

	public static boolean hasCsvFormat(MultipartFile file) {
		return CSV_TYPE.equals(file.getContentType());
	}

	public static List<User> csvToStudents(MultipartFile file)
	throws Exception {
		NativeJavaCsvParser parser = new NativeJavaCsvParser();
		List<String[]> result = parser.readFile((File) file, 1);
		List<User> userToCreate = new ArrayList<>();

		for (String[] userArr : result) {
			String email = userArr[0];
			String firstName = userArr[1];
			String lastName = userArr[2];
			String phoneNumber = userArr[3];
			String roleType = userArr[4];

			User newUser = new User();
			newUser.setEmail(email);
			newUser.setUsername(email);
			newUser.setFirstname(firstName);
			newUser.setLastname(lastName);
			newUser.setPhonenumber(phoneNumber);

			newUser.setUserid(0);
			userToCreate.add(newUser);
		}

		return userToCreate;
	}

}
