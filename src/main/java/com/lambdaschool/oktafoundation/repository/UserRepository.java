package com.lambdaschool.oktafoundation.repository;


import com.lambdaschool.oktafoundation.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


/**
 * The CRUD repository connecting User to the rest of the application
 */
public interface UserRepository
		extends CrudRepository<User, Long> {

	/**
	 * Find a user based off over username
	 *
	 * @param username the name (String) of user you seek
	 *
	 * @return the first user object with the name you seek
	 */
	User findByUsername(String username);

	Optional<User> findByEmail(String email);

	/**
	 * Find all users whose name contains a given substring ignoring case
	 *
	 * @param name the substring of the names (String) you seek
	 *
	 * @return List of users whose name contain the given substring ignoring case
	 */
	List<User> findByUsernameContainingIgnoreCase(String name);

	@Query(value = "SELECT * FROM usercourses uc JOIN users u ON uc.userid = u.userid WHERE courseid = :courseid",
			nativeQuery = true)
	List<User> findEnrolledUsers(long courseid);

	@Query(value = "SELECT * FROM users WHERE userid NOT IN (SELECT userid FROM usercourses WHERE courseid=:courseid)",
			nativeQuery = true)
	List<User> findNotEnrolledUsers(long courseid);

	@Query(value = "SELECT * FROM users u WHERE CONCAT(u.username, ' ', u.firstname, ' ', u.lastname, ' ', u.email) " +
	               "ILIKE %:query% ", nativeQuery = true)
	List<User> search(String query);

}
