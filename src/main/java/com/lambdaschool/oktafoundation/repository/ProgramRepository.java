package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
public interface ProgramRepository
		extends CrudRepository<Program, Long> {

	Optional<Program> findByProgramnameIgnoreCase(String name);

	List<Program> findByTags_tag_titleIgnoreCase(String name);

	List<Program> findByTags_tag_titleLikeIgnoreCase(String name);

	List<Program> findByTags_tag_tagid(long tagid);

	@Query(value = "SELECT * FROM PROGRAMS p WHERE p.userid = :userid\n", nativeQuery = true)
	List<Program> findProgramsByUserid(long userid);

	@Modifying
	@Query(value = "DELETE FROM PROGRAMS WHERE PROGRAMID = :programid", nativeQuery = true)
	void deleteById(long programid);

}
