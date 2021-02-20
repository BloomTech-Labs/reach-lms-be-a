package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProgramRepository extends CrudRepository<Program, Long>
{
    Program findByProgramnameIgnoreCase(String name);

    @Query(value = "SELECT * FROM PROGRAMS\n" +
        "JOIN USERS u\n" +
        "WHERE u.userid = :userid\n", nativeQuery = true)
    List<Program> findProgramsByUserid(long userid);
}
