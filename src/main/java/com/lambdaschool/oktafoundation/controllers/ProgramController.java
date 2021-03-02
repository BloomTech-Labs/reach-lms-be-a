package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ProgramController
{
    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramRepository programRepos;

    @GetMapping(value = "/programs", produces = "application/json")
    public ResponseEntity<?> listAllPrograms()
    {
        List<Program> myPrograms = programService.findAll();

        return new ResponseEntity<>(myPrograms, HttpStatus.OK);
    }

    @GetMapping(value = "/programs/program/{programId}", produces = "application/json")
    public ResponseEntity<?> getProgramById(@PathVariable Long programId)
    {
        Program program = programService.findProgramsById(programId);

        return new ResponseEntity<>(program, HttpStatus.OK);
    }
    
    @GetMapping(value = "/programs/program/name/{programName}", produces = "application/json")
    public ResponseEntity<?> getProgramByName(@PathVariable String programName)
    {
        Program p = programService.findProgramsByName(programName);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }



    @GetMapping(value = "/programs/{userid}", produces = "application/json")
    public ResponseEntity<?> getProgramsByUserId(@PathVariable long userid)
    {
        List<Program> pList = programRepos.findProgramsByUserid(userid);

        return new ResponseEntity<>(pList, HttpStatus.OK);
    }

    @PostMapping(value = "/programs/{userid}/program", consumes = {"application/json"}, produces = "application/json")
    public ResponseEntity<?> addNewProgram(@PathVariable long userid, @Valid @RequestBody Program newProgram) throws URISyntaxException
    {
        newProgram.setProgramid(0);
        newProgram = programService.save(userid, newProgram);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newProgramURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{programid}")
                .buildAndExpand(newProgram.getProgramid())
                .toUri();
        responseHeaders.setLocation(newProgramURI);

        return new ResponseEntity<>(newProgram, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/programs/program/{programid}", consumes = "application/json")
    public ResponseEntity<?> editEntireProgram(@Valid @RequestBody Program editProgram, @PathVariable Long programid)
    {
        editProgram.setProgramid(programid);
        Program newtProgram = programService.update(editProgram, programid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/programs/program/{programid}", consumes = "application/json")
    public ResponseEntity<?> editPartialProgram(@RequestBody Program editPartialProgram, @PathVariable Long programid)
    {
        programService.update(editPartialProgram, programid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/programs/program/{programid}")
    public ResponseEntity<?> deleteProgram(@PathVariable long programid)
    {
        programRepos.findById(programid).orElseThrow(() -> new ResourceNotFoundException("Program id with id" + programid + " Not found!"));
        programRepos.deleteById(programid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
