package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface TagRepository
		extends CrudRepository<Tag, Long> {

	List<Tag> findByPrograms_program_programid(long programid);
	List<Tag> findByPrograms_program_programname(String programname);
	Optional<Tag> findByTitle(String title);

}
