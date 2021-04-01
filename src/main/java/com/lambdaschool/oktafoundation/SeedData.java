package com.lambdaschool.oktafoundation;


import com.lambdaschool.oktafoundation.models.Module;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.TagRepository;
import com.lambdaschool.oktafoundation.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@ConditionalOnProperty(prefix = "command.line.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
@Component
public class SeedData
		implements CommandLineRunner {

	/**
	 * Connects the Role Service to this process
	 */
	@Autowired
	RoleService roleService;

	/**
	 * Connects the user service to this process
	 */
	@Autowired
	UserService userService;

	@Autowired
	StudentTeacherService studentTeacherService;

	@Autowired
	ProgramService programService;

	@Autowired
	CourseService courseService;

	@Autowired
	ModuleService moduleService;

	@Autowired
	TagRepository tagRepository;

	/**
	 * Generates test, seed data for our application
	 * First a set of known data is seeded into our database.
	 * Second a random set of data using Java Faker is seeded into our database.
	 * Note this process does not remove data from the database. So if data exists in the database
	 * prior to running this process, that data remains in the database.
	 *
	 * @param args The parameter is required by the parent interface but is not used in this process.
	 */
	@Transactional
	@Override
	public void run(String[] args)
	throws Exception {
		roleService.deleteAll();
		Role adminRole   = new Role(RoleType.ADMIN.name(), RoleType.ADMIN);
		Role teacherRole = new Role(RoleType.TEACHER.name(), RoleType.TEACHER);
		Role studentRole = new Role(RoleType.STUDENT.name(), RoleType.STUDENT);

		adminRole   = roleService.save(adminRole);
		teacherRole = roleService.save(teacherRole);
		studentRole = roleService.save(studentRole);

		// USER llama001@maildrop.cc
		User llama001 = new User("llama001@maildrop.cc", "llama001@email.com", "llama1", "LLAMA_001", "9876543210");
		llama001.getRoles()
				.add(new UserRoles(llama001, adminRole));
		llama001 = userService.save(llama001);

		// TEACHER USER reach.lms.test+Teach001@gmail.com
		User teacher001 = new User("reach.lms.test+Teach001@gmail.com",
				"reach.lms.test+Teach001@gmail.com",
				"Teacher001",
				"TEACHER_001",
				null
		);

		teacher001.getRoles()
				.add(new UserRoles(teacher001, teacherRole));

		// STUDENT USER reach.lms.test+Student001@gmail.com
		User student001 = new User("reach.lms.test+Student001@gmail.com",
				"reach.lms.test+Student001@gmail.com",
				"Student001",
				"STUDENT_001",
				null
		);

		student001.getRoles()
				.add(new UserRoles(student001, studentRole));

		teacher001 = userService.save(teacher001);
		student001 = userService.save(student001);

		// USER reach.lms.test@gmail.com
		User reachRoot = new User("reach.lms.test@gmail.com",
				"reach.lms.test@gmail.com",
				"reach_root",
				"reach_root",
				"1234567890"
		);
		reachRoot.getRoles()
				.add(new UserRoles(reachRoot, adminRole));

		Tag tag_cs             = new Tag("Computer Science", "#000000");
		Tag tag_dataStructures = new Tag("Data Structures", "#FF0000");
		Tag tag_higherEd       = new Tag("Higher Education");
		Tag tag_webDev         = new Tag("Web Development", "#0000FF");
		Tag tag_python         = new Tag("Python");


		String cs39_description = //
				"Welcome to Computer Science!\n" + "\n" +
				"We're here to practice one skill: ***problem-solving*** (without looking up the " +
				"answers on the Internet).\n" + "\n>That's it!\n\n" +
				"To accomplish this, we'll look at various techniques, data structures, and algorithms and use them to attack problems relentlessly. Most importantly, we'll utilize a problem-solving framework called U.P.E.R. This defines a process you can follow to solve any problem without looking up the answer.\n" +
				"\n" +
				"Attacking problems relentlessly is the main attribute that separates the good developers from the average ones. If you find that a problem is being difficult and is stubbornly refusing to be solved, keep on attacking.\n" +
				"\n" + "\"An expert has failed more times than a beginner has even tried.\"\n" + "\n" +
				"We stress this so much because hiring managers look for problem-solving skills in the individuals they hire. They might use various interviewing techniques, including whiteboarding, but they are all looking to ascertain your problem-solving skill level.\n" +
				"\n" +
				"In Computer Science, none of the answers are meant to be obvious. With every single one of them, we intend that you initially have no idea how to solve it, but then you attack it to resolution with the techniques described in the problem-solving framework.\n" +
				"\n" + "Remember that as you go through the program: if you have no idea how to solve a " +
				"problem when you first look at it, that's by design. You're in good company. Start relentlessly attacking!";

		String cs39_shorter = "Welcome to Computer Science! We're here to practice one skill: problem-solving";
		Program program_cs39 = new Program("CS39--Lambda Computer Science", "Computer Science", cs39_shorter);


		String csFundamentals_description = //
				"#Overview\n" +
				"During this sprint, we will introduce you to some fundamental Computer Science fundamentals. First, we will introduce you to Python (the language we will be using throughout Computer Science). Second, you will learn about Lambda's Problem-Solving Framework, which we call U.P.E.R. Third, you will learn about Big O notation and analyzing an algorithm's time and space complexity. Last, you will learn about the basics of computer memory.\n" +
				"\n" + "All of these topics lay down a crucial base for the other three sprints in " +
				"Computer Science. You will rely on your Python skills, problem-solving abilities, ability to analyze time and space complexity, and your mental model for computer memory throughout the rest of the course.";


		String pythonBasics_description = "In this module, you will learn all of the fundamentals of the Python programming language. After completing this module, you will have all the basics that you need to get started using Python to solve algorithmic code challenges and deepen your understanding and skill set related to programming.";
		String pythonBasics_content = // Markdown content for the Installing Python objective in pythonBasics
				"# Objective 01 - Install Python 3 on their local machine, run the interactive prompt in the terminal, and run Python files through the interpreter\n" +
				"\n" + "## Overview\n" +
				"To get started writing Python, you need to be able to use the Python interpreter. There are essentially two primary approaches:\n" +
				"\n" + "  1. Install Python on your local machine.\n" + "\n" +
				"  2. Use a website that allows you to access the Python interpreter online.\n" + "\n" +
				"Below, we will walk through how to install Python for your specific OS and refer to some preferred websites that give you access to the interpreter online.\n" +
				"\n" + "## Follow Along\n" + "\n" + "### Windows\n" + "\n" +
				"Windows machines usually do not ship with Python installed. Installing on Windows is pretty simple.\n" + "\n" +
				"  1. Download the latest Python 3 Installer from [python.org](https://www.python.org/downloads/windows/) (make sure you pay attention to 32-bit vs. 64-bit and select the right one for your machine).\n" +
				"  \n" +
				"  2. Run the installer and make sure you check the box that says \"Add Python 3.x to PATH\" to ensure that you place the interpreter in your execution path.\n" +
				"\n" + "### Linux\n" + "\n" +
				"Most likely, your Linux distribution already has Python installed. However, it is likely to be Python 2 and not Python 3.\n" +
				"\n" +
				"You can determine what version you have by opening a terminal and typing `python --version`. If the version shown is `Python 2.x.x`, then you want to install the latest version of Python 3.\n" +
				"\n" +
				"The procedure for installing the latest version of Python depends on which distribution of Linux you are running.\n" +
				"\n" +
				"Use [this article](https://realpython.com/installing-python/#reader-comments) to find instructions specific to your Linux distribution.\n" +
				"\n" + "### macOS / Mac OS X\n" + "\n" +
				"Current versions of macOS include a version of Python 2, but you want to be using Python 3.\n" + "\n" +
				"The best way to install Python 3 on macOS is to use the Homebrew package manager.\n" + "\n" +
				"#### Install Homebrew\n" + "\n" +
				"  1. Go to [http://brew.sh/](http://brew.sh/) and select the Homebrew bootstrap code under \"Install Homebrew\" and copy the complete command to your clipboard.\n" +
				"  \n" + "  2. Open a terminal window, paste the Homebrew bootstrap code, and hit \"Enter.\"\n" + "  \n" +
				"  3. It may take some time to install Homebrew, so you need to wait for that process to complete before moving on.\n" +
				"\n" + "After Homebrew has finished its installation process, you then need to install Python.\n" + "\n" +
				"#### Install Python\n" + "  \n" +
				"  1. Open a terminal and run the following command brew install python3. This command will download and install the latest version of Python.\n" +
				"\n" +
				"  2. Ensure that everything was installed correctly by opening a terminal window and running the command pip3.\n" +
				"  \n" +
				"  3. If you see help text from Python's \"pip\" package manager, you have a working Python installation.\n" +
				"\n" + "\n" + "### Online Interpreters\n" + "\n" +
				"Here are a few websites that give you online access to the Python interpreter:\n" + "\n" +
				"  - [Repl.it](https://repl.it/)\n" + "  - [Trinket](https://trinket.io/)\n" +
				"  - [Python Fiddle](http://pythonfiddle.com/)\n" +
				"  - [Python.org Online Console](https://www.python.org/shell)\n" +
				"  - [Python Anywhere](https://www.pythonanywhere.com/)\n" + "\n" + "## Challenge\n" + "\n" +
				"Before doing anything else, you should now follow the instructions above (those that apply to your OS) and make sure you have a working installation of Python on your machine.\n" +
				"\n" + "## Additional Resources\n" + "\n" +
				"  - [https://docs.python.org/3/using/index.html](https://docs.python" + ".org/3/using/index.html)\n";

		String listComprehensions_content = // Markdown content for the list comprehensions objective
				"# Objective 11 - Use list comprehensions\n" + "\n" + "## Overview\n" +
				"List comprehensions are a potent tool. With a list comprehension, you can create a new list based on another list in a single, highly readable line.\n" +
				"\n" + "The format of a list comprehension follows this syntax:\n" + "\n" + "```python\n" +
				"[<map expression> for <name> in <sequence expression> if <filter expression>]\n" + "```\n" + "\n" +
				"## Follow Along\n" + "\n" +
				"If you are using a `for` loop to map a list onto a new list or filter an existing list, a list comprehension can be a better option.\n" +
				"\n" +
				"Here is an example of replacing a `for` loop used to map word lengths with a single line with a list comprehension.\n" +
				"\n" + "```python\n" + "sentence = \"Every moment is a fresh beginning.\"\n" + "words = sentence.split()\n" +
				"word_lengths = []\n" + "\n" + "# Using a for loop\n" + "for word in words:\n" +
				"    word_lengths.append(len(word))\n" + "\n" +
				"print(words)        # ['Every', 'moment', 'is', 'a', 'fresh', 'beginning.']\n" +
				"print(word_lengths) # [5, 6, 2, 1, 5, 10]\n" + "\n" + "# Using a list comprehension\n" +
				"word_lengths = [len(word) for word in words]\n" + "\n" + "print(word_lengths) # [5, 6, 2, 1, 5, 10]\n" +
				"```\n" + "\n" +
				"Here is an example of replacing a `for` loop used to filter out odd numbers from a list with a list comprehension.\n" +
				"\n" + "```python\n" + "numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n" + "even_numbers = []\n" + "\n" +
				"# Using a for loop\n" + "for number in numbers:\n" + "    if number % 2 == 0:\n" +
				"        even_numbers.append(number)\n" + "\n" + "print(even_numbers) # [2, 4, 6, 8, 10]\n" + "\n" +
				"# Using a list comprehension\n" + "even_numbers = [number for number in numbers if number % 2 == 0]\n" + "\n" +
				"print(even_numbers) # [2, 4, 6, 8, 10]\n" + "```\n" + "\n" +
				"You can also write a list comprehension that maps and filters simultaneously. Let's go back to our sentence example and only track word lengths that are greater than 2.\n" +
				"\n" + "```python\n" + "sentence = \"Every moment is a fresh beginning.\"\n" + "words = sentence.split()\n" +
				"word_lengths = []\n" + "\n" + "# Using a for loop\n" + "for word in words:\n" + "    if len(word) > 2:\n" +
				"        word_lengths.append(len(word))\n" + "\n" +
				"print(words)        # ['Every', 'moment', 'is', 'a', 'fresh', 'beginning.']\n" +
				"print(word_lengths) # [5, 6, 5, 10]\n" + "\n" + "# Using a list comprehension\n" +
				"word_lengths = [len(word) for word in words if len(word) > 2]\n" + "\n" +
				"print(word_lengths) # [5, 6, 5, 10]\n" + "```\n" + "\n" + "##Challenge\n" + "\n" +
				"- [Open in Replit](https://replit.com/@mdmccarley89/cs-unit-1-sprint-1-module-1-list-comprehensions)\n" +
				"\n" + "\n" + "##Additional Resources\n" + "\n" +
				"- [https://realpython.com/list-comprehension-python/](https://realpython" + ".com/list-comprehension-python/)";

		Module problemSolving;
		Module timeSpaceComplexity;
		Module computerMemoryBasics;

		Course csStructsAlgos1;
		Module arrStringManipulation;
		Module linkedLists;
		Module queuesStacks;
		Module searchRecursion;

		Course csStructsAlgos2;
		Module binarySearchTrees;
		Module treeTraversal;
		Module graphs1;
		Module graphs2;

		Course csStructsAlgos3;
		Module hashTables1;
		Module hashTables2;


		// PROGRAMS
		Program program1 = new Program("Program1", "12th grade", "This is program 1");
		Program program2 = new Program("Program2", "12th grade", "This is program 2");

		reachRoot.getPrograms()
				.add(program1);
		reachRoot.getPrograms()
				.add(program2);
		reachRoot.getPrograms()
				.add(program_cs39);

		reachRoot = userService.save(reachRoot);

		program1.addTag(tag_cs);
		program1.addTag(tag_higherEd);
		program2.addTag(tag_cs);
		program_cs39.addTag(tag_cs);
		program_cs39.addTag(tag_dataStructures);
		program_cs39.addTag(tag_python);

		program1     = programService.save(reachRoot.getUserid(), program1);
		program2     = programService.save(reachRoot.getUserid(), program2);
		program_cs39 = programService.save(reachRoot.getUserid(), program_cs39);

		Course course1 = new Course("Course1", "COURSE_1", "This course1", program1);
		Course course2 = new Course("Course2", "COURSE_2", "This is course #2", program1);
		Course course3 = new Course("Course3", "COURSE_3", "This is course #3", program2);
		Course course_cs39_1_fundamentals = new Course("Computer Science Fundamentals",
				"CS39_1.1",
				csFundamentals_description,
				program_cs39
		);
		course_cs39_1_fundamentals.setTag(tag_python);

		course1.addUser(teacher001);
		course2.addUser(new UserCourses(teacher001, course2));
		course1.addUser(student001);
		course2.addUser(student001);

		course_cs39_1_fundamentals.addUser(List.of(student001, teacher001));

		course1.setTag(tag_cs);
		course2.setTag(tag_higherEd);

		course1                    = courseService.save(course1.getProgram()
				.getProgramid(), course1);
		course2                    = courseService.save(course2.getProgram()
				.getProgramid(), course2);
		course3                    = courseService.save(course3.getProgram()
				.getProgramid(), course3);
		course_cs39_1_fundamentals = courseService.save(program_cs39.getProgramid(), course_cs39_1_fundamentals);

		Module module1 = new Module("Module1", "This is module #1", "Content for module #1", course1);
		Module module2 = new Module("Module2", "This is module #2", "Content for module #2", course1);
		Module module3 = new Module("Module3", "This is module #3", "Content for module #3", course1);
		Module module4 = new Module("Module4", "This is module #4", "Content for module #4", course2);

		module1 = moduleService.save(module1.getCourse()
				.getCourseid(), module1);
		module2 = moduleService.save(module2.getCourse()
				.getCourseid(), module2);
		module3 = moduleService.save(module3.getCourse()
				.getCourseid(), module3);
		module4 = moduleService.save(module4.getCourse()
				.getCourseid(), module4);
		Module pythonBasics = new Module("Python Basics",
				pythonBasics_description,
				pythonBasics_content,
				course_cs39_1_fundamentals
		);

		Module listComprehensions = new Module("List Comprehensions",
				"List Comprehensions are PREMIUM... almost as good as Dictionary Comprehensions",
				listComprehensions_content,
				course_cs39_1_fundamentals
		);

		pythonBasics = moduleService.save(pythonBasics.getCourse()
				.getCourseid(), pythonBasics);

		listComprehensions = moduleService.save(course_cs39_1_fundamentals.getCourseid(), listComprehensions);
	}

}