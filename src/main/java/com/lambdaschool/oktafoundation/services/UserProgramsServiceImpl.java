package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserPrograms;
import com.lambdaschool.oktafoundation.models.UserProgramsId;
import com.lambdaschool.oktafoundation.repository.UserProgramsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "userProgramsService")
public class UserProgramsServiceImpl implements UserProgramsService
{
    @Autowired
    UserProgramsRepository userProgramsRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProgramService programService;

    @Override
    public UserPrograms save(long userid, long programid)
    {
        Program program = programService.findProgramsById(programid);
        User user = userService.findUserById(userid);
        UserPrograms userPrograms = userProgramsRepository.findById(new UserProgramsId(user.getUserid(), program.getProgramid())).orElse(new UserPrograms(user, program));

        return userProgramsRepository.save(userPrograms);
    }

    @Override
    public List<Program> findUserPrograms(long userid)
    {
        List<UserPrograms> userPrograms = new ArrayList<>();
        userProgramsRepository.findAll().iterator().forEachRemaining(userPrograms::add);
        List<Program> programs = new ArrayList<>();
        for (UserPrograms p : userPrograms)
        {
            if (p.getUser().getUserid() == userid)
            {
                programs.add(p.getProgram());
            }
        }
        return programs;
    }
}
