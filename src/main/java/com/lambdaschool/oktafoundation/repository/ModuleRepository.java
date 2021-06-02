package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ModuleRepository
		extends JpaRepository<Module, Long> {

	Optional<Module> findByModuleNameIgnoreCase(String name);

	List<Module> findModulesByCourse_CourseId(long id);

}
