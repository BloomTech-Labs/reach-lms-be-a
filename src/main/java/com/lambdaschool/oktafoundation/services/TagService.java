package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Tag;

import java.util.List;


public interface TagService {

	// GET ALL
	List<Tag> getAll();


	// GET TAG BY ...
	// tag id
	Tag get(long tagid);
	// tag title
	Tag get(String title);


	// GET TAGS BY PROGRAM
	// program id
	List<Tag> getByProgram(long programid);
	// program name
	List<Tag> getByProgram(String programname);


	// GET TAGS BY COURSE
	// course id
	List<Tag> getByCourse(long courseid);
	// course name
	List<Tag> getByCourse(String coursename);


	// SAVE HAS A DOUBLE OVERLOAD
	Tag save(
			long programid,
			Tag tag
	);
	Tag save(
			Tag tag,
			long programid
	);


	// REPLACE HAS A TRIPLE OVERLOAD
	Tag replace(Tag tag);
	Tag replace(
			long tagid,
			Tag tag
	);
	Tag replace(
			Tag tag,
			long tagid
	);


	// UPDATE HAS A TRIPLE OVERLOAD
	Tag update(Tag tag);
	Tag update(
			Tag tag,
			long tagid
	);
	Tag update(
			long tagid,
			Tag tag
	);


	// DELETE HAS A DOUBLE OVERLOAD
	void delete(long tagid);
	void delete(Tag tag);

	// DELETE ALL
	void deleteAll();

}
