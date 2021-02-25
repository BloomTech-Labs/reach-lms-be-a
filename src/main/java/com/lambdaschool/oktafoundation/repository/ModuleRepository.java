package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Module;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ModuleRepository extends CrudRepository<Module, Long>
{
    Module findByModulenameIgnoreCase(String name);

    List<Module> findModulesByCourse_Courseid(long id);
}
