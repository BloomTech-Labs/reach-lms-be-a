package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.UserPrograms;
import com.lambdaschool.oktafoundation.models.UserProgramsId;
import org.springframework.data.repository.CrudRepository;

public interface UserProgramsRepository extends CrudRepository<UserPrograms, UserProgramsId>
{
}
