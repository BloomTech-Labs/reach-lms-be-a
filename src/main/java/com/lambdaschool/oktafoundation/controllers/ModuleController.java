package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.repository.ModuleRepository;
import com.lambdaschool.oktafoundation.services.ModuleService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class ModuleController
{
    @Autowired
    ModuleRepository modulerepos;

    @Autowired
    ModuleService moduleService;

    @GetMapping(value = "/modules", produces = "application/json")
    public ResponseEntity<?> getAllModules()
    {
        List<Module> modules = moduleService.findAll();

        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping(value = "/modules/module/{moduleId}", produces = "application/json")
    public ResponseEntity<?> getModuleById(@PathVariable Long moduleId)
    {
        Module module = moduleService.findModulesById(moduleId);

        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    @GetMapping(value = "/modules/module/{moduleName]", produces = "application/json")
    public ResponseEntity<?> getModuleByName(@PathVariable String moduleName)
    {
        Module m = moduleService.findModulesByName(moduleName);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @GetMapping(value = "/modules/{courseId}", produces = "application/json")
    public ResponseEntity<?> getModulesByCourseId(@PathVariable Long courseId)
    {
        List<Module> allModules = new ArrayList<>();
        modulerepos.findModulesByCourse_Courseid(courseId).iterator().forEachRemaining(allModules::add);

        return new ResponseEntity<>(allModules, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping(value = "/modules/{courseId}/module", produces = "application/json")
    public ResponseEntity<?> addNewModule(@PathVariable long courseId, @Valid @RequestBody Module newModule) throws URISyntaxException
    {
        newModule.setModuleid(0);
        newModule = moduleService.save(courseId, newModule);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newModuleURI = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{moduleid}")
                .buildAndExpand(newModule.getModuleid())
                .toUri();
        responseHeaders.setLocation(newModuleURI);

        return new ResponseEntity<>(newModule, responseHeaders, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PatchMapping(value = "/modules/{moduleId}", consumes = "application/json")
    public ResponseEntity<?> updateModule(@PathVariable long moduleId, @RequestBody Module newModule)
    {
        newModule.setModuleid(moduleId);
        moduleService.update(moduleId, newModule);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping(value = "/modules/{moduleId}", consumes = "application/json")
    public ResponseEntity<?> updateFullModule(@PathVariable long moduleId, @RequestBody Module newModule)
    {
        newModule.setModuleid(moduleId);
        moduleService.update(moduleId, newModule);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping(value = "/modules/{moduleId}")
    public ResponseEntity<?> deleteModuleById(@PathVariable long moduleId)
    {
        modulerepos.findById(moduleId).orElseThrow(() -> new ResourceNotFoundException("Module with id" + moduleId + " not found."));
        moduleService.delete(moduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
