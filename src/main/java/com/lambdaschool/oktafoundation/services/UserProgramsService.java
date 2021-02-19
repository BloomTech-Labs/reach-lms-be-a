package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.UserPrograms;

import java.util.List;

public interface UserProgramsService
{
    List<Program> findUserPrograms(long userid);

    UserPrograms save(long userid, long programid);
}
