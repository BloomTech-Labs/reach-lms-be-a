package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProgramRepository extends CrudRepository<Program, Long>
{
    Program findByProgramnameIgnoreCase(String name);

    @Query(value = "SELECT * FROM PROGRAMS p\n" +
        "WHERE p.userid = :userid\n", nativeQuery = true)
    List<Program> findProgramsByUserid(long userid);

    @Modifying
    @Query(value = "DELETE FROM PROGRAMS\n" +
        "WHERE PROGRAMID = :programid\n", nativeQuery = true)
    void deleteById(long programid);
}
