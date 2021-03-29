package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ProgramNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.UserNotFoundException;
import com.lambdaschool.oktafoundation.models.Course;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.ProgramTags;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.repository.TagRepository;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service(value = "programService")
public class ProgramServiceImpl
		implements ProgramService {

	@Autowired
	ProgramRepository programRepository;

	@Autowired
	UserRepository userrepos;

	@Autowired
	TagRepository tagRepository;

	@Override
	public Program save(
			long userid,
			Program program
	)
	throws ProgramNotFoundException, UserNotFoundException {
		Program newProgram = new Program();

		if (program.getProgramid() != 0) {
			findProgramsById(program.getProgramid()); // throws if program not found
			newProgram.setProgramid(program.getProgramid());
		}

		newProgram.setProgramname(program.getProgramname());
		newProgram.setProgramtype(program.getProgramtype());
		newProgram.setProgramdescription(program.getProgramdescription());

		for (ProgramTags programTags : program.getTags()) {
			newProgram.addTag(programTags.getTag());
		}

		newProgram.getCourses()
				.clear();

		for (Course course : program.getCourses()) {
			newProgram.getCourses()
					.add(new Course(course.getCoursename(),
							course.getCoursedescription(),
							course.getCoursedescription(),
							newProgram
					));
		}
		User currentUser = userrepos.findById(userid)
				.orElseThrow(() -> new UserNotFoundException(userid));
		newProgram.setUser(currentUser);
		return programRepository.save(newProgram);
	}

	@Override
	public List<Program> findProgramsByTagName(String title) {
		List<Program> programs = new ArrayList<>();
		programRepository.findByTags_tag_titleLikeIgnoreCase(title)
				.iterator()
				.forEachRemaining(programs::add);
		return programs;
	}

	@Override
	public List<Program> findAll() {
		List<Program> programs = new ArrayList<>();
		programRepository.findAll()
				.iterator()
				.forEachRemaining(programs::add);
		return programs;
	}

	@Override
	public Program findProgramsById(long id)
	throws ProgramNotFoundException {
		return programRepository.findById(id)
				.orElseThrow(() -> new ProgramNotFoundException(id));
	}

	@Override
	public Program findProgramsByName(String name)
	throws ProgramNotFoundException {
		return programRepository.findByProgramnameIgnoreCase(name)
				.orElseThrow(() -> new ProgramNotFoundException(name));
	}

	@Override
	public void delete(long id)
	throws ProgramNotFoundException {
		findProgramsById(id); // throws if not found
		programRepository.deleteById(id);
	}

	@Override
	public Program update(
			Program program,
			long id
	)
	throws ProgramNotFoundException {
		Program oldProgram = findProgramsById(id); // throws if not found

		if (program.getProgramname() != null) {
			oldProgram.setProgramname(program.getProgramname());
		}

		if (program.getProgramtype() != null) {
			oldProgram.setProgramtype(program.getProgramtype());
		}

		if (program.getProgramdescription() != null) {
			oldProgram.setProgramdescription(program.getProgramdescription());
		}

		if (program.getTags()
				    .size() > 0) {
			oldProgram.getTags()
					.clear();
			for (ProgramTags programTags : program.getTags()) {
				oldProgram.addTag(programTags.getTag());
			}
		}

		return programRepository.save(oldProgram);

	}


}
