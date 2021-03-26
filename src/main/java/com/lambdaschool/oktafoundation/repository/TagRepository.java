package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Tag;
import org.springframework.data.repository.CrudRepository;


public interface TagRepository
		extends CrudRepository<Tag, Long> {}
