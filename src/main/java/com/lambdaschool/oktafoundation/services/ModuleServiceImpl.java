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
		return moduleRepository.findByModulenameIgnoreCase(moduleName)
				.orElseThrow(() -> new ModuleNotFoundException(moduleName));
	}

	@Override
	public Module findModulesById(long id)
	throws ModuleNotFoundException {
		return find(id);
	}

	@Override
	public void replaceMarkdown(
			Long moduleid,
			String markdown
	) {
		Module module = find(moduleid); // throws if no such module
		module.setModulecontent(markdown);
		moduleRepository.save(module);
	}

	@Override
	public Module save(
			long courseId,
			Module module
	)
	throws ModuleNotFoundException, CourseNotFoundException {
		Module newModule = new Module();
		if (module.getModuleid() != 0) {
			find(module.getModuleid()); // throws if no such module
			newModule.setModuleid(module.getModuleid());
		}
		newModule.setModulename(module.getModulename());
		newModule.setModuledescription(module.getModuledescription());
		newModule.setModulecontent(module.getModulecontent());

		Course course = courseService.get(courseId); // throws if no such course
		if (course != null) {
			newModule.setCourse(course);
		}
		return moduleRepository.save(newModule);
	}

	@Override
	public Module update(
			long id,
			Module module
	) throws ModuleNotFoundException {
		Module newModule = find(id); // throws if no such module

		if (module.getModulename() != null) {
			newModule.setModulename(module.getModulename());
		}

		if (module.getModuledescription() != null) {
			newModule.setModuledescription(module.getModuledescription());
		}

		if (module.getModulecontent() != null) {
			newModule.setModulecontent(module.getModulecontent());
		}
		return moduleRepository.save(newModule);
	}

	@Override
	public void delete(long id)
	throws ModuleNotFoundException {
		find(id); // throws if no such module
		moduleRepository.deleteById(id);
	}

}
