package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.services.CsvService;
import com.lambdaschool.oktafoundation.utils.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UploadCsvController {

	@Autowired
	CsvService csvService;

	@PostMapping("/upload/csv/course-student-roster/{courseid}")
	public ResponseEntity<?> uploadFile(
			@PathVariable
					Long courseid,
			@RequestParam("file")
					MultipartFile file
	) {
		String message;
		if (CsvHelper.hasCsvFormat(file)) {
			try {
				csvService.save(file, courseid);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return new ResponseEntity<>(message, HttpStatus.CREATED);
			} catch (Exception e) {
				message = "Could not upload the file " + file.getOriginalFilename() + "!";
				return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
			}
		}
		message = "Please upload a CSV File!";
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/upload/csv/student-roster")
	public ResponseEntity<?> uploadFile(
			@RequestParam("file")
					MultipartFile file
	) {
		String message;

		if (CsvHelper.hasCsvFormat(file)) {
			try {
				csvService.save(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
			}
		}
		message = "Please upload a CSV File!";
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

}
