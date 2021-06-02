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

	Optional<Program> findByProgramNameIgnoreCase(String name);

	List<Program> findByTags_tag_titleIgnoreCase(String name);

	List<Program> findByTags_tag_titleLikeIgnoreCase(String name);

	List<Program> findByTags_tag_tagId(long tagId);

	@Query(value = "SELECT * FROM programs p WHERE p.user_id = :userId\n", nativeQuery = true)
	List<Program> findProgramsByUserId(long userId);

	@Modifying
	@Query(value = "DELETE FROM programs WHERE program_id = :programId", nativeQuery = true)
	void deleteById(long programId);

}
