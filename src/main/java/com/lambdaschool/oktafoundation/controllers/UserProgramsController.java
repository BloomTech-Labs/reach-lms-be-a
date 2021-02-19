package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.services.UserProgramsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class UserProgramsController
{
    @Autowired
    private UserProgramsService userProgramsService;

    @GetMapping(value = "/users/{userid}/programs", produces = "application/json")
    public ResponseEntity<?> getUserPrograms(@PathVariable long userid)
    {
        List<Program> programs = userProgramsService.findUserPrograms(userid);

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }
}
