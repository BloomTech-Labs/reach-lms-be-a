package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.ProgramIn;

import java.util.List;


public interface ProgramService {

	Program save(
			long userid,
			Program program
	);

	Program save(
			long userid,
			ProgramIn programIn
	);

	List<Program> findProgramsByTagName(String title);

	List<Program> findAll();

	Program findProgramsById(long id);

	Program findProgramsByName(String name);

	void delete(long id);

	Program update(
			ProgramIn programIn,
			long programId
	);

	Program update(
			Program program,
			long id
	);

}
