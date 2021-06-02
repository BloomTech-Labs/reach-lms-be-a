package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.CourseNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.ModuleNotFoundException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service(value = "moduleService")
public class ModuleServiceImpl
		implements ModuleService {

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private CourseService courseService;

	@Override
	public List<Module> findAll() {
		List<Module> modules = new ArrayList<>();
		moduleRepository.findAll()
				.iterator()
				.forEachRemaining(modules::add);
		return modules;
	}

	@Override
	public Module find(long moduleId)
	throws ModuleNotFoundException {
		return moduleRepository.findById(moduleId)
				.orElseThrow(() -> new ModuleNotFoundException(moduleId));
	}

	@Override
	public Module find(String moduleName)
	throws ModuleNotFoundException {
		return moduleRepository.findByModuleNameIgnoreCase(moduleName)
				.orElseThrow(() -> new ModuleNotFoundException(moduleName));
	}

	@Override
	public Module findModulesById(long id)
	throws ModuleNotFoundException {
		return find(id);
	}

	@Override
	public void replaceMarkdown(
			Long moduleId,
			String markdown
	) {
		Module module = find(moduleId); // throws if no such module
		module.setModuleContent(markdown);
		moduleRepository.save(module);
	}

	@Override
	public Module save(
			long courseId,
			Module module
	)
	throws ModuleNotFoundException, CourseNotFoundException {
		Module newModule = new Module();
		if (module.getModuleId() != 0) {
			find(module.getModuleId()); // throws if no such module
			newModule.setModuleId(module.getModuleId());
		}
		newModule.setModuleName(module.getModuleName());
		newModule.setModuleDescription(module.getModuleDescription());
		newModule.setModuleContent(module.getModuleContent());

		Course course = courseService.get(courseId); // throws if no such course
		if (course != null) {
			newModule.setCourse(course);
		}
		return moduleRepository.save(newModule);
	}

	@Override
	public Module update(
			long moduleId,
			Module module
	)
	throws ModuleNotFoundException {
		Module newModule = find(moduleId); // throws if no such module

		if (module.getModuleName() != null) {
			newModule.setModuleName(module.getModuleName());
		}

		if (module.getModuleDescription() != null) {
			newModule.setModuleDescription(module.getModuleDescription());
		}

		if (module.getModuleContent() != null) {
			newModule.setModuleContent(module.getModuleContent());
		}
		return moduleRepository.save(newModule);
	}

	@Override
	public void delete(long moduleId)
	throws ModuleNotFoundException {
		find(moduleId); // throws if no such module
		moduleRepository.deleteById(moduleId);
	}

	@Override
	public void deleteAll() {
		moduleRepository.deleteAll();
	}

}
