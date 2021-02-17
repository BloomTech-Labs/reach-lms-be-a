package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.data.repository.CrudRepository;

public interface ProgramRepository extends CrudRepository<Program, Long>
{
    Program findByNameContainingIgnoreCase(String name);
}
