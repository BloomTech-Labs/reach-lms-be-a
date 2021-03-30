package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.exceptions.RoleNotSufficientException;
import com.lambdaschool.oktafoundation.exceptions.UserNotFoundException;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Implements UserService Interface
 */
@Transactional
@Service(value = "userService")
public class UserServiceImpl
		implements UserService {

	/**
	 * Connects this service to the User table.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Connects this service to the Role table
	 */
	@Autowired
	private RoleService roleService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private HelperFunctions helperFunctions;

	@Override
	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		// findAll returns an iterator set.
		// iterate over the iterator set and add each element to an array list.
		userRepository.findAll()
				.iterator()
				.forEachRemaining(list::add);
		return list;
	}

	@Override
	public List<User> search(String query) {
		List<User> users = new ArrayList<>();
		if (query != null) {
			userRepository.search(query)
					.iterator()
					.forEachRemaining(users::add);
		} else {
			return findAll();
		}
		return users;
	}

	@Override
	public List<User> findByNameContaining(String username) {

		return userRepository.findByUsernameContainingIgnoreCase(username.toLowerCase());
	}

	@Override
	public User findUserById(long id)
	throws UserNotFoundException {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByName(String name)
	throws UserNotFoundException {
		User uu = userRepository.findByUsername(name.toLowerCase());
		if (uu == null) {
			throw new UserNotFoundException(name);
		}
		return uu;
	}

	@Transactional
	@Override
	public void delete(long id)
	throws UserNotFoundException {
		findUserById(id); // this will throw if user not found
		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public User save(User user) {

		User newUser = new User();

		if (user.getUserid() != 0) {
			findUserById(user.getUserid()); // throws if user not found
			newUser.setUserid(user.getUserid());
		}
		newUser.setUsername(user.getUsername()
				.toLowerCase());
		newUser.setEmail(user.getEmail());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		newUser.setPhonenumber(user.getPhonenumber());

		newUser.getRoles()
				.clear();
		if (user.getRole() != null) {
			Role newRole = roleService.findByName(user.getRole()
					.name());
			newUser.getRoles()
					.add(new UserRoles(newUser, newRole));
		}

		if (user.getRoles()
				    .size() > 0) {
			for (UserRoles ur : user.getRoles()) {
				Role addRole = roleService.findByName(ur.getRole()
						.getName());
				newUser.getRoles()
						.add(new UserRoles(newUser, addRole));
			}

		}

		newUser.getCourses()
				.clear();
		for (UserCourses userCourses : user.getCourses()) {
			Course course = courseService.findCourseById(userCourses.getCourse()
					.getCourseid());
			newUser.getCourses()
					.add(new UserCourses(newUser, course));
		}

		return userRepository.save(newUser);
	}

	@Transactional
	@Override
	public User update(
			User userIn,
			long id
	) {
		User currentUser = findUserById(id);
		// update own thing OR the calling user is ADMIN
		if (helperFunctions.getCurrentPriorityRole() == RoleType.ADMIN ||
		    helperFunctions.isAuthorizedToMakeChange(currentUser.getUsername())) {
			return updateFunctionality(currentUser, userIn);
		} else {
			// note we should never get to this line but is needed for the compiler
			// to recognize that this exception can be thrown
			throw new ResourceNotFoundException("This user is not authorized to make change");
		}
	}

	@Transactional
	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Transactional
	@Override
	public User updateRole(
			User user,
			RoleType newRole
	) {
		User userToUpdate = findUserById(user.getUserid()); // throws if user not found
		if (userToUpdate.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users cannot be changed from ADMIN");
		} else {
			Role roleToUse = roleService.findByName(newRole.name());
			userToUpdate.getRoles()
					.add(new UserRoles(userToUpdate, roleToUse));
		}
		return userToUpdate;
	}

	@Transactional
	public User updateFunctionality(
			User currentUser,
			User userIn
	) {
		if (userIn.getUsername() != null) {
			currentUser.setUsername(userIn.getUsername()
					.toLowerCase());
		}

		if (userIn.getEmail() != null) {
			currentUser.setEmail(userIn.getEmail()
					.toLowerCase());
		}

		if (userIn.getFirstname() != null) {
			currentUser.setFirstname(userIn.getFirstname());
		}

		if (userIn.getLastname() != null) {
			currentUser.setLastname(userIn.getLastname());
		}
		if (userIn.getPhonenumber() != null) {
			currentUser.setPhonenumber(userIn.getPhonenumber());
		}

		if (userIn.getRole() != null) {
			Role newRole = roleService.findByName(userIn.getRole()
					.name());
			currentUser.getRoles()
					.add(new UserRoles(currentUser, newRole));
		}

		if (userIn.getRoles()
				    .size() > 0) {
			for (UserRoles ur : userIn.getRoles()) {
				Role addRole = roleService.findByName(ur.getRole()
						.getName());
				currentUser.getRoles()
						.add(new UserRoles(currentUser, addRole));
			}

		}

		if (userIn.getCourses()
				    .size() > 0) {
			currentUser.getCourses()
					.clear();
			for (UserCourses userCourses : userIn.getCourses()) {
				Course course = courseService.findCourseById(userCourses.getCourse()
						.getCourseid());
				currentUser.getCourses()
						.add(new UserCourses(currentUser, course));
			}
		}
		return userRepository.save(currentUser);
	}


}
