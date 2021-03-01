package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "programService")
public class ProgramServiceImpl
    implements ProgramService
{
    @Autowired
    ProgramRepository programRepository;

    @Autowired
    UserRepository userrepos;

    @Override
    public Program save(long userid, Program program) throws ResourceNotFoundException
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

        newProgram.getCourses()
            .clear();

        for(Course course : program.getCourses())
        {
            newProgram.getCourses()
                .add(new Course(course.getCoursename(),
                    course.getCoursedescription(),
                    course.getCoursedescription(),
                    newProgram));
        }



        User currentUser = userrepos.findById(userid)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + userid + "not found !"));
        if(currentUser != null){
            newProgram.setUser(currentUser);
        }
        return programRepository.save(newProgram);
    }

    @Override
    public List<Program> findAll()
    {
        List<Program> programs = new ArrayList<>();
        programRepository.findAll().iterator().forEachRemaining(programs::add);
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
        Program pp = programRepository.findByProgramnameIgnoreCase(name);
        if (pp == null)
        {
            throw new ResourceNotFoundException("Program name " + name + " not found.");
        }
        return pp;
    }


    @Override
    public Program update(Program program, long id)
    {
        Program oldProgram = findProgramsById(id);

        if (program.getProgramname() != null)
        {
            oldProgram.setProgramname(program.getProgramname());
        }

        if (program.getProgramtype() != null)
        {
            oldProgram.setProgramtype(program.getProgramtype());
        }

        if (program.getProgramdescription() != null)
        {
            oldProgram.setProgramdescription(program.getProgramdescription());
        }

        return programRepository.save(oldProgram);

    }


    @Override
    public void delete(long id) throws ResourceNotFoundException
    {
        programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program with id " + id + " not found."));
        programRepository.deleteById(id);


    }


}
