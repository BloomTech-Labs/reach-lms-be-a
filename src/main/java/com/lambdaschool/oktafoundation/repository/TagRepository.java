package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TagRepository
		extends CrudRepository<Tag, Long> {
	List<Tag> findByPrograms_program_programid(long programid);
}
