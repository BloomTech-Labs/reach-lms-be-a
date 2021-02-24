package com.lambdaschool.oktafoundation.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModuleRepository extends CrudRepository<Module, Long>
{
    List<Module> findModulesByCourse_Courseid(long courseid);
}
