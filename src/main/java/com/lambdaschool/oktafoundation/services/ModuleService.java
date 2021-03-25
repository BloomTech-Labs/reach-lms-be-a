package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Module;

import java.util.List;


public interface ModuleService {

	List<Module> findAll();

	Module findModulesById(long id);

	Module findModulesByName(String name);

	Module save(
			long id,
			Module module
	);

	Module update(
			long id,
			Module module
	);

	void delete(long id);

}
