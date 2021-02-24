package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Module;

import java.util.List;

public interface ModuleService
{
    List<Module> findAll();

    Module findModuleById(long moduleid);

    Module save(long moduleid, Module module);

    Module update(long moduleid, Module module);

    void delete(long moduleid);
}
