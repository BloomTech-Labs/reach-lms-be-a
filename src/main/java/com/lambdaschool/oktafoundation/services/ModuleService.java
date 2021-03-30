package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Module;

import java.util.List;


public interface ModuleService {

	List<Module> findAll();

	Module find(long moduleId);

	Module find(String name);


	Module findModulesById(long id);

	void replaceMarkdown(Long moduleid, String markdown);

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
