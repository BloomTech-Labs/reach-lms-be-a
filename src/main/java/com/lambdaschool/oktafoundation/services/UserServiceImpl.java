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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
	private UserRepository userrepos;

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
		userrepos.findAll()
				.iterator()
				.forEachRemaining(list::add);
		return list;
	}

	@Override
	public List<User> findByNameContaining(String username) {

		return userrepos.findByUsernameContainingIgnoreCase(username.toLowerCase());
	}

	public User findUserById(long id)
	throws UserNotFoundException {
		return userrepos.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public User findByName(String name) {
		User uu = userrepos.findByUsername(name.toLowerCase());
		if (uu == null) {
			throw new ResourceNotFoundException("User name " + name + " not found!");
		}
		return uu;
	}

	@Transactional
	@Override
	public void delete(long id)
	throws UserNotFoundException {
		findUserById(id); // this will throw if user not found
		userrepos.deleteById(id);
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

		return userrepos.save(newUser);
	}

	@Transactional
	@Override
	public User update(
			User user,
			long id
	) {
		User currentUser = findUserById(id);

		// update own thing
		// admin update
		if (helperFunctions.isAuthorizedToMakeChange(currentUser.getUsername())) {
			if (user.getUsername() != null) {
				currentUser.setUsername(user.getUsername()
						.toLowerCase());
			}

			if (user.getEmail() != null) {
				currentUser.setEmail(user.getEmail()
						.toLowerCase());
			}

			if (user.getFirstname() != null) {
				currentUser.setFirstname(user.getFirstname());
			}

			if (user.getLastname() != null) {
				currentUser.setLastname(user.getLastname());
			}
			if (user.getPhonenumber() != null) {
				currentUser.setPhonenumber(user.getPhonenumber());
			}

			if (user.getRole() != null) {
				Role newRole = roleService.findByName(user.getRole()
						.name());
				currentUser.getRoles()
						.add(new UserRoles(currentUser, newRole));
			}

			if (user.getRoles()
					    .size() > 0) {
				for (UserRoles ur : user.getRoles()) {
					Role addRole = roleService.findByName(ur.getRole()
							.getName());
					currentUser.getRoles()
							.add(new UserRoles(currentUser, addRole));
				}

			}
			return userrepos.save(currentUser);
		} else {
			// note we should never get to this line but is needed for the compiler
			// to recognize that this exception can be thrown
			throw new ResourceNotFoundException("This user is not authorized to make change");
		}
	}

	@Transactional
	@Override
	public void deleteAll() {
		userrepos.deleteAll();
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
	@Override
	public User replaceUserEnrollments(
			Long userid,
			List<Long> courseids
	) {
		User userToUpdate = findUserById(userid);
		if (userToUpdate.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users are not attached at the course-level");
		} else {
			Set<Long>        hashedIds  = new HashSet<>(courseids);
			Set<UserCourses> newCourses = new HashSet<>();
			User             finalUserToUpdate = userToUpdate;
			hashedIds.forEach(courseid -> {
//				Course course = courseService.findCourseById(courseid);
				newCourses.add(new UserCourses(finalUserToUpdate, courseService.findCourseById(courseid)));
			});
			userToUpdate.setCourses(newCourses);
			userToUpdate = update(userToUpdate, userToUpdate.getUserid());
			return userToUpdate;
		}

	}


}
