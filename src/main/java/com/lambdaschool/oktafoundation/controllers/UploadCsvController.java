package com.lambdaschool.oktafoundation.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UploadCsvController {

	@PostMapping("/upload/csv/student-roster")
	public ResponseEntity<?> uploadFile() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
