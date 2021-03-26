package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Tag;

import java.util.List;


public interface TagService {

	List<Tag> getAll();
	Tag getById(long tagid);
	List<Tag> getTagsByProgramId(long programid);

}
