package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.RoleNotSufficientException;
import com.lambdaschool.oktafoundation.modelAssemblers.UserModelAssembler;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import com.lambdaschool.oktafoundation.services.OktaSDKService;
import com.lambdaschool.oktafoundation.services.RoleService;
import com.lambdaschool.oktafoundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * The entry point for clients to access user data
 */
@RestController
public class UserController {

	/**
	 * Using the User service to process user data
	 */
	@Autowired
	private UserService userService;

	@Autowired
	private UserModelAssembler userModelAssembler;

	@Autowired
	private RoleService roleService;

	@Autowired
	private HelperFunctions helperFunctions;

	@Autowired
	private OktaSDKService okta;

	/**
	 * Returns a list of all users
	 * <br>Example: <a href="http://localhost:2019/users/users">http://localhost:2019/users/users</a>
	 *
	 * @return JSON list of all users with a status of OK
	 *
	 * @see UserService#findAll() UserService.findAll()
	 */
	@GetMapping(value = "/users", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<User>>> listAllUsers(
			@RequestParam(required = false)
					String query
	) {
		List<EntityModel<User>> myUsers = userService.search(query)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(myUsers,
				linkTo(methodOn(UserController.class).listAllUsers(query)).withSelfRel()
		);

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	/**
	 * Returns a single user based off a user id number
	 * <br>Example: http://localhost:2019/users/user/7
	 *
	 * @param userId The primary key of the user you seek
	 *
	 * @return JSON object of the user you seek
	 *
	 * @see UserService#findUserById(long) UserService.findUserById(long)
	 */
	@GetMapping(value = "/users/user/{userId}", produces = "application/json")
	public ResponseEntity<EntityModel<User>> getUserById(
			@PathVariable
					Long userId
	) {
		EntityModel<User> user = userModelAssembler.toModel(userService.findUserById(userId));
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Return a user object based on a given username
	 * <br>Example: <a href="http://localhost:2019/users/user/name/cinnamon">http://localhost:2019/users/user/name/cinnamon</a>
	 *
	 * @param userName the name of user (String) you seek
	 *
	 * @return JSON object of the user you seek
	 *
	 * @see UserService#findByName(String) UserService.findByName(String)
	 */
	@GetMapping(value = "/users/user/name/{userName}", produces = "application/json")
	public ResponseEntity<EntityModel<User>> getUserByName(
			@PathVariable
					String userName
	) {
		EntityModel<User> user = userModelAssembler.toModel(userService.findByName(userName));
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Returns a list of users whose username contains the given substring
	 * <br>Example: <a href="http://localhost:2019/users/user/name/like/da">http://localhost:2019/users/user/name/like/da</a>
	 *
	 * @param userName Substring of the username for which you seek
	 *
	 * @return A JSON list of users you seek
	 *
	 * @see UserService#findByNameContaining(String) UserService.findByNameContaining(String)
	 */
	@GetMapping(value = "/users/user/name/like/{userName}", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<User>>> getUserLikeName(
			@PathVariable
					String userName
	) {
		List<EntityModel<User>> userEntities = userService.findByNameContaining(userName)
				.stream()
				.map(userModelAssembler::toModel)
				.collect(Collectors.toList());
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(userEntities,
				linkTo(methodOn(UserController.class).getUserByName(userName)).withSelfRel()
		);
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@PostMapping("/users/create-user")
	public ResponseEntity<?> addMinUser(
			@Valid
			@RequestBody
					MinimumUser minimumUser
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole != RoleType.ADMIN) {
			throw new RoleNotSufficientException("Your role is not sufficient to create a new user");
		}
		User newUser = new User();
		newUser.setUserid(0);
		Role role = roleService.findByName(minimumUser.getRoleType()
				.name());
		newUser.getRoles()
				.add(new UserRoles(newUser, role));
		newUser.setEmail(minimumUser.getEmail());
		if (minimumUser.getUsername() != null) {
			newUser.setUsername(minimumUser.getUsername());
		} else {
			newUser.setUsername(minimumUser.getEmail());
		}
		if (minimumUser.getFirstname() != null) {
			newUser.setFirstname(minimumUser.getFirstname());
		} else {
			newUser.setFirstname(minimumUser.getEmail());
		}
		if (minimumUser.getLastname() != null) {
			newUser.setLastname(minimumUser.getLastname());
		} else {
			newUser.setLastname(minimumUser.getEmail());
		}
		newUser = userService.save(newUser);

		com.okta.sdk.resource.user.User oktaUser = okta.createOktaUser(newUser.getEmail(),
				newUser.getFirstname(),
				newUser.getLastname(),
				newUser.getRole()
						.name()
		);

		HttpHeaders responseHeaders = new HttpHeaders();
		URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{userid}")
				.buildAndExpand(newUser.getUserid())
				.toUri();
		responseHeaders.setLocation(newUserURI);
		return new ResponseEntity<>(newUser, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * Given a complete User Object, create a new User record and accompanying useremail records
	 * and user role records.
	 * <br> Example: <a href="http://localhost:2019/users/user">http://localhost:2019/users/user</a>
	 *
	 * @param newuser A complete new user to add including emails and roles.
	 *                roles must already exist.
	 *
	 * @return A location header with the URI to the newly created user and a status of CREATED
	 *
	 * @see UserService#save(User) UserService.save(User)
	 */
	@PostMapping(value = "/users/user", consumes = "application/json")
	public ResponseEntity<?> addNewUser(
			@Valid
			@RequestBody
					User newuser
	) {
		newuser.setUserid(0);
		newuser = userService.save(newuser);
		// set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{userid}")
				.buildAndExpand(newuser.getUserid())
				.toUri();
		responseHeaders.setLocation(newUserURI);
		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * Given a complete User Object
	 * Given the user id, primary key, is in the User table,
	 * replace the User record and Useremail records.
	 * Roles are handled through different endpoints
	 * <br> Example: <a href="http://localhost:2019/users/user/15">http://localhost:2019/users/user/15</a>
	 *
	 * @param updateUser A complete User including all emails and roles to be used to
	 *                   replace the User. Roles must already exist.
	 * @param userid     The primary key of the user you wish to replace.
	 *
	 * @return status of OK
	 *
	 * @see UserService#save(User) UserService.save(User)
	 */
	@PutMapping(value = "/users/user/{userid}", consumes = "application/json")
	public ResponseEntity<?> updateFullUser(
			@Valid
			@RequestBody
					User updateUser,
			@PathVariable
					long userid
	) {
		updateUser.setUserid(userid);
		userService.save(updateUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Updates the user record associated with the given id with the provided data. Only the provided fields are affected.
	 * Roles are handled through different endpoints
	 * If an email list is given, it replaces the original emai list.
	 * <br> Example: <a href="http://localhost:2019/users/user/7">http://localhost:2019/users/user/7</a>
	 *
	 * @param updateUser An object containing values for just the fields that are being updated. All other fields are left NULL.
	 * @param userid     The primary key of the user you wish to update.
	 *
	 * @return A status of OK
	 *
	 * @see UserService#update(User, long) UserService.update(User, long)
	 */
	@PatchMapping(value = "/users/user/{userid}", consumes = "application/json")
	public ResponseEntity<?> updateUser(
			@RequestBody
					User updateUser,
			@PathVariable
					long userid
	) {
		userService.update(updateUser, userid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Deletes a given user along with associated emails and roles
	 * <br>Example: <a href="http://localhost:2019/users/user/14">http://localhost:2019/users/user/14</a>
	 *
	 * @param userid the primary key of the user you wish to delete
	 *
	 * @return Status of OK
	 */
	@DeleteMapping(value = "/users/user/{userid}")
	public ResponseEntity<?> deleteUserById(
			@PathVariable
					long userid
	) {
		userService.delete(userid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Returns the User record for the currently authenticated user based off of the supplied access token
	 * <br>Example: <a href="http://localhost:2019/users/getuserinfo">http://localhost:2019/users/getuserinfo</a>
	 *
	 * @param authentication The authenticated user object provided by Spring Security
	 *
	 * @return JSON of the current user. Status of OK
	 *
	 * @see UserService#findByName(String) UserService.findByName(authenticated user)
	 */
	@GetMapping(value = "/users/getuserinfo", produces = {"application/json"})
	public ResponseEntity<EntityModel<User>> getCurrentUserInfo(Authentication authentication) {
		EntityModel<User> user = userModelAssembler.toModel(userService.findByName(authentication.getName()));
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PatchMapping(value = "/users/user/{userid}/{roleType}")
	public ResponseEntity<?> updateUserRole(
			@PathVariable
					Long userid,
			@Valid
			@PathVariable
					RoleType roleType
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		User     userToEdit      = userService.findUserById(userid);
		if (callingUserRole != RoleType.ADMIN) {
			throw new RoleNotSufficientException("You are not an ADMIN. You may not update another user's role");
		} else if (userToEdit.getRole() == RoleType.ADMIN) {
			throw new RoleNotSufficientException("ADMIN users cannot edit other ADMIN users");
		} else {
			userToEdit = userService.updateRole(userToEdit, roleType);
		}
		return new ResponseEntity<>(userToEdit, HttpStatus.OK);

	}


}