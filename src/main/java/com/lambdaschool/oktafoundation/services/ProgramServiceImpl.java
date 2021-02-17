package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "programService")
public class ProgramServiceImpl implements ProgramService
{
    @Autowired
    ProgramRepository programRepository;

    @Override
    public Program save(Program program)
    {
        Program newProgram = new Program();

        if (program.getProgramid() != 0)
        {
            programRepository.findById(program.getProgramid())
                    .orElseThrow(() -> new ResourceNotFoundException("Program with id " + program.getProgramid() + " not found."));
            newProgram.setProgramid(program.getProgramid());
        }

        newProgram.setProgramname(program.getProgramname());
        newProgram.setProgramtype(program.getProgramtype());
        newProgram.setProgramdescription(program.getProgramdescription());

        return programRepository.save(newProgram);
    }

    @Override
    public List<Program> findAll()
    {
        List<Program> programs = new ArrayList<>();
        programRepository.findAll().iterator().forEachRemaining(programs::add);
        System.out.println(programs);
        return programs;
    }

    @Override
    public Program findProgramsById(long id)
    {
        return programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program with id " + id + " not found."));
    }

    @Override
    public Program findProgramsByName(String name)
    {
        Program pp = programRepository.findByNameContainingIgnoreCase(name);
        if (pp == null)
        {
            throw new ResourceNotFoundException("Program name " + name + " not found.");
        }
        return pp;
    }

    @Override
    public void delete(long id)
    {
        programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program with id " + id + " not found."));
        programRepository.deleteById(id);
    }

    @Override
    public Program update(Program program, long id)
    {
        return null;
    }


}
