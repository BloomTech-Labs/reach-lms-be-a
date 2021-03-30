package com.lambdaschool.oktafoundation.utils;


import com.lambdaschool.oktafoundation.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class CsvHelper {

	public static String CSV_TYPE = "text/csv";

	public static boolean hasCsvFormat(MultipartFile file) {
		return CSV_TYPE.equals(file.getContentType());
	}

	public static File convert(MultipartFile file)
	throws IOException {
		if (file == null) {
			throw new RuntimeException("File cannot be null!");
		} else {
			File             convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
			FileOutputStream fos           = new FileOutputStream(convertedFile);
			fos.write(file.getBytes());
			fos.close();
			return convertedFile;
		}
	}

	public static List<User> csvToStudents(MultipartFile file)
	throws Exception {
		NativeJavaCsvParser parser        = new NativeJavaCsvParser();
		File                convertedFile = convert(file);
		List<String[]>      result        = parser.readFile(convertedFile, 1);
		List<User>          userToCreate  = new ArrayList<>();

		for (String[] userArr : result) {
			String email       = userArr[0];
			String firstName   = userArr[1];
			String lastName    = userArr[2];
			String phoneNumber = userArr[3];
			String roleType    = userArr[4];

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
