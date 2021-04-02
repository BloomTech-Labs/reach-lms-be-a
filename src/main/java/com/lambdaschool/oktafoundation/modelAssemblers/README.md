# Representational Model Assemblers

---

# Table of Contents

- [Overview](#overview)
    - [Why?](#why?)
    - [How?](#how?)

- [General Example](#general-example)
    - [Before Model Assembler](#before-implementing-the-model-assembler--get-entitiesentityentityid)
    - [Writing Model Assembler](#now-lets-write-the-model-assembler-itself)
    - [After Implementing](#after-implementing-the-model-assembler--get-entitiesentityentityid)

- [Full Implementation Example](#full-implementation)
    - [Models](#models)
    - [Actions & Permissions](#actions--permissions)
    - [Endpoints](#endpoints)
    - [Code for Models](#code-for-models)
        - [RoleType Enum](#modelsroletypejava--roletype-enum)
        - [User Model](#modelsuserjava--user-entity-class)
        - [Course Model](#modelscoursejava--course-entity)
        - [Module Model](#modelsmodulejava--module-entity)
    - [Quick Look at Repo & Service Layer](#quick-look-at-the-repo--service-layer)
    - [Code for Controllers (BEFORE Model-Assembler)](#code-for-controllers--before-model-assemblers)
        - [User Controller](#usercontrollerjava-before-model-assembler)
        - [Course Controller](#coursecontrollerjava-before-model-assembler)
        - [Module Controller](#modulecontrollerjava-before-model-assembler)
    - [Code for Model Assemblers](#code-for-model-assemblers)
        - [User Model Assembler](#usermodelassemblerjava)
        - [Course Model Assembler] — Not Included
        - [Module Model Assembler] — Not Included
    - [Code for Controllers (AFTER Model-Assembler)](#code-for-controllers----after-model-assembler)
        - [User Controller](#usercontrollerjava-after-model-assembler)
        - [Course Controller] — Not Included
        - [Module Controller] — Not Included

- [Additional Resources](#resources)

---

# Overview

## Why?

These Representational Model assemblers are essentially helper components that allow us to represent any domain type (
i.e., any class in our `/models/*` folder with an `@Entity` annotation) as a RESTful representational model with
relational links to other related endpoints.

The benefits:

1. You'll actually be able to claim you've worked with and built upon a RESTful API
2. The response models are practically self-documenting, your client could surf your endpoints without looking at
   documentation once
3. The shape of your data is much cleaner in terms of handling Time & Space complexity. Instead of crazy, nested data,
   your clients will have top-level information about the entity they hit as well as the key to go access relevant
   related information!

## How?

Every `*ModelAssembler` class should have the following:

- You must include the `@Component` annotation at the class level if you'd like to `@Autowire` this class anywhere.

- `implements` the  `RepresentationModelAssembler<E, EntityModel<E>>`
  from `org.springframework.hateoas.server.RepresentationModelAssembler`.

- The "diamond notation" above denotes that the `RepresentationalModelAssembler` will convert whatever class-type `E`
  entity to an `EntityModel<E>` (i.e, an EntityModel OF that entity). `SomeModel` in this case should be somewhere in
  our `/models/` directory, and it should have an `@Entity` annotation at the class-level.

- The only method that MUST be overridden with the `@Override` annotation is the `toModel` method. This method should
  take in a basic Entity class instance (the same entities you're used to building) and add any links that should belong
  to it.

# General Example

This example will focus on the basic essentials for writing the `ModelAssembler` class itself. If you want the full flow
of how to USE the model assembler, skip to [Full Example — The Complete Implementation Flow](#full-implementation)

## Before Implementing the Model Assembler — `GET "/entities/entity/{entityId}"`

Here's what one single entity might've looked like before we implement the model assembler.

```json
{
  "entityId": 4,
  "entityName": "This is just an example of a plain entity!",
  "entityType": "POJO",
  "hobbies": [
    {
      "hobbyId": 1,
      "hobbyName": "Rocket League",
      "timeSpent": "Too damn much"
    },
    {
      "hobbyId": 2,
      "hobbyName": "Coding",
      "timeSpent": "All of it"
    }
  ]
}
```

## Now, let's write the Model Assembler itself

```java
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

// the following imports are imported statically for improved readability below
// the reason this is possible is b/c these methods are defined with a 'public static' modifier in WebMvcLinkBuilder
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component // needs to be a component to @Autowire it anywhere
public class ExampleAssembler
		implements RepresentationModelAssembler<Entity, EntityModel<Entity>> {

	// this method will take in some POJO entity and 
	// transform it into our EntityModel representation
	@Override
	public EntityModel<Entity> toModel(Entity entity) {

		EntityModel<Entity> entityModel = EntityModel.of(entity,
				// this is where we can add any links to this entity
				//
				// this link is a link to some controller method in the 'ExampleController' class
				// the method in this case will return one single entity by its id... 
				// so this might be an endpoint that looked something like GET "/entities/entity/{entityId}"
				// 
				// with the following line of code, we are adding a link to our EntityModel so that  
				// our client has access to further information that may be related to this entity...
				// The link below provides a "link to self" so to speak, which is why '.withSelfRel()' is called.
				// if our endpoint location actually was GET "/entities/entity/{entityId}", then the following
				// line of code will attach a PROPERTY within the '_links' object with a full URI that 
				// a client could go hit to GET THIS SPECIFIC ENTITY: "http://localhost:2019/entities/entitiy/4"
				linkTo(methodOn(ExampleController.class).getEntityByEntityId(entity.getId())).withSelfRel(),

				// one powerful ability that comes from this pattern is the ability to link to any relationships
				// that this entity might have. Now the client has the agency to go get a collection of all the 
				// hobbies associated with this entity!
				linkTo(methodOn(HobbyController.class).getHobbiesByEntityId(entity.getId())).withRel("hobbies")
		);

		// we also have the ability to add links conditionally
		// if the entity in question has an ID that is less than 10, we'll add the following links
		if (entity.getId() < 10) {
			// this method lets us add new links onto the entity. Very useful for handling conditional logic
			entityModel.add(
					// link to the method in the controller that returns all the entities with a single-digit ID 
					linkTo(methodOn(ExampleController.class).getEntitiesWithSingleDigitId()).withRel("single_digit_id") //
			);

		}

		return entityModel;

	}

}
```

## After Implementing the Model Assembler — `GET "/entities/entity/{entityId}"`

Here's what it would look like if we hit `GET "/entities/entity/4"` after implementing our model assembler.

```json
{
  "entityId": 4,
  "entityName": "This is just an example of a plain entity!",
  "entityType": "POJO",
  "_links": {
    "self": {
      "href": "http://localhost:2019/entities/entity/4"
    },
    "hobbies": {
      "href": "http://localhost:2019/hobbies/by-entity-id/4"
    },
    "single_digit_id": {
      "href": "http://localhost:2019/entities/single-digit"
    }
  }
}
```

We had added in some conditional logic that attached the `single_digit_id` only if the entity had an `entityId` that was
less than `10`. So what would our response look like if we hit `GET "/entities/entity/12"`?

```json
{
  "entityId": 4,
  "entityName": "This is just an example of a plain entity!",
  "entityType": "POJO",
  "_links": {
    "self": {
      "href": "http://localhost:2019/entities/entity/4"
    },
    "hobbies": {
      "href": "http://localhost:2019/hobbies/by-entity-id/4"
    }
  }
}
```

--- 

<a name="full-implementation"></a>

# Full Implementation

Now that you've seen a basic example of what these classes do and what the data looks like as a result, let's look at a
slightly more comprehensive example.

Let's look at a more stripped-down version of Reach LMS.

This example will walk through a `UserModelAssembler` class that builds a representational model for the `User` class.

Then we'll implement and use that `UserModelAssembler` in our `UserController` class.

## Models

### `User`

- `userid`
- `username`
- `firstname`
- `lastname`
- `roleType` — One of three: `ADMIN`, `TEACHER`, and `STUDENT`
- List of `Course` — Any `Course` this user is attached to

### `Course`

- `courseid`
- `coursename`
- `coursedescription`
- List of `Module` — All the `Module` entities nested within this `Course`
- List of `User` — All the `Users` this course is attached to

### `Modules`

- `moduleid`
- `modulename`
- `Course` — The `Course` wherein this `Module` resides

## Actions & Permissions

The interesting part of this example is that the `User` class is fairly flexible. Every single person in our system is
actually a `User`. Their abilities come from what `roleType` they have.

### `ADMIN` users can...

- Create a `User` of any role — `ADMIN`, `STUDENT`, or `TEACHER`
- Change the `roleType` of any `TEACHER` or `STUDENT` to any `roleType` — `ADMIN`, `STUDENT`, or `TEACHER`
    - Note that `ADMIN` users cannot change the `roleType` of OTHER `ADMIN` users
- Delete `User` of roleType `STUDENT` or `TEACHER`
- Attach and detach `TEACHER` and `STUDENT` users to and from any `Course`
- Create new `Course`

### `TEACHER` and `ADMIN` users can...

- Edit `Course` information
- Add/Edit/Delete any `Module` inside of a `Course` that they teach (if `TEACHER`) or own (if `ADMIN`).
- Attach and Detach `STUDENT` users from any `Course` over which they have ownership

### `STUDENT` and `TEACHER` and `ADMIN` users can...

- View all the `Course` entities that they are attached to or have ownership over.
- View all the `Module` entities inside each `Course` to which they (the user) are attached.

## Endpoints

### User Controller

- Get all Users
    - `GET "/users"`
- Get User
    - `GET "/users/{userid}"`
- Create New User
    - `POST "/users/user"`
- Replace User
    - `PUT "/users/user/{userid}"`
- Edit User
    - `PATCH "/users/user/{userid}"`
- Delete User
    - `DELETE "/users/user/{userid}"`
- Replace User's Role
    - `PUT "/users/user/{userid}/role/{newRoleType}"`

### Course Controller

- `GET "/courses"`
- `GET "/courses/course/{courseid}"`
- `GET "/courses/course/name/{coursename}"`
- `POST "/courses/course"`
- `PUT "/courses/course/{courseid}"`
- `PATCH "/courses/course/{courseid}"`
- `DELETE "/courses/course/{courseid}"`
- Attach User to Course
    - `PUT "/users/user/{userid}/course/{courseid}"`
- Detach User from Course
    - `DELETE "/users/user/{userid}/course/{courseid}"`
- Get all Users associated with a Course
    - `GET "/users/enrolled-in/{courseid}"`
- Get any users NOT associated with a Course
    - `GET "/users/not-enrolled-in/{courseid}"`

### Module Controller

- `GET "/modules"`
- `GET "/modules/module/{moduleid}`
- `GET "/modules/by-course/{courseid}`
- `POST "modules/to-course/{courseid}"`
- `DELETE "/modules/module/{moduleid}`

---

## Code for Models

Now that we know a bit more about what we're building, let's look at the entity class for each model. This is the same
goodness that you all have seen since the first week of Unit 4

--- 

### `/models/RoleType.java` — RoleType Enum

I know some of you aren't going to be familiar with Enums. For anone unfamiliar with the concept of enums, they are
simply a special data type allows for pre-defined groups of constants.

This is really convenient for little collections of data that (1.) don't change throughout our application and (2.) are
referenced a lot. That makes the `enum` a PERFECT choice for our user's `role`!!!

Unfold the following to see some comparisons between `enum` and alternative methods

<details>

  <summary>Examples of why we're using an Enum class as opposed to the alternative approaches </summary>

  ---

### Why Use Enums??? Just take a look at the alternatives...

### Using a plain `String`

Imagine trying to check to see if a user was an `ADMIN`... if we used a plain `String`, it would look like this:

  ```java
  public class UserRoleWithString { // don't worry bout this line

	public static void main(String[] args) { // don't worry bout this line


		User user = new User();
		user.setRole("ADMIN");

		// Here's what it might look like to check to see if our user was an ADMIN
		if (user.getRole()
				.equals("ADMIN")) {
			System.out.println("User is an ADMIN!!");
		} //
		else if (user.getRole()
				.equals("TEACHER")) {
			System.out.println("User is a TEACHER!!");
		} //
		else {
			System.out.println("User must be a STUDENT!");
		}
	}

}
  ```

### Using the `UserRoles` join class

And if we used the `UserRoles` that we've usually used, it would be even grosser:

  ```java
  public class UserRoleWithUserRolesJoin {

	public static void main(String[] args) {

		// in this case, role would be Set<UserRoles> roles = new HashSet<>();
		User user      = new User();
		Role adminRole = new Role("ADMIN");
		adminRole = roleService.save(adminRole);
		user.getRoles()
				.add(new UserRoles(user, adminRole));
		user = userService.save(user);

		// this is what it might look like to check our user's role
		// note that this could be ANYWHERE in our application... NOT just in Seed Data 
		// so we would not have access to the 'adminRole' declared above
		// we'd have to find the role
		Role adminRole   = roleService.findByName("ROLE_ADMIN");
		Role teacherRole = roleService.findByName("ROLE_TEACHER");
		Role studentRole = roleService.findByName("ROLE_STUDENT");

		// then, to check if our User's Set of roles contained a specific one, we'd
		// have to take advantage of native HashSet methods (like 'contains') and the 'equals' method
		// defined in UserRoles.java
		boolean containsAdminRole = user.getRoles()
				.contains(new UserRoles(user.getUserid(), adminRole.getRoleid()));
		boolean containsTeacherRole = user.getRoles()
				.contains(new UserRoles(user.getUserid(), teacherRole.getRoleid()));
		boolean containsStudentRole = user.getRoles()
				.contains(new UserRoles(user.getUserid(), studentRole.getRoleid()));

		if (containsAdminRole) {
			System.out.println("The user is an ADMIN");
		}
		if (containsTeacherRole) {
			System.out.println("The user is a TEACHER");
		}
		if (containsStudentRole) {
			System.out.println("The user is a STUDENT");
		}
	}

}
  ```

  ---

### Enum Stuff

```java

import com.lambdaschool.oktafoundation.models.RoleType;


public class UsingEnumExample {

	public static void main(String[] args) {

		System.out.println(RoleType.ADMIN); // prints "ADMIN"
		System.out.println(RoleType.TEACHER); // prints "TEACHER"
		System.out.println(RoleType.STUDENT); // prints "STUDENT"

		System.out.println(RoleType.ADMIN.name()); // prints "ADMIN"

		System.out.println(RoleType.ADMIN.ordinal()); // prints 0
		System.out.println(RoleType.TEACHER.ordinal()); // prints 1
		System.out.println(RoleType.STUDENT.ordinal()); // prints 2

		// !!! ERROR --- RoleType.ADMIN is of type RoleType, not String
		String this_is_an_error = RoleType.ADMIN; // THIS WOULD NOT COMPILE

		// .name() will return the name of the member (ADMIN) as a String
		String adminRoleString = RoleType.ADMIN.name();

		// .ordinal() returns the value associated with that member
		int adminRoleOrdinal = RoleType.ADMIN.ordinal();

		// ALWAYS false -- but this is to show that you can COMPARE
		// the members of an Enum with double equals.
		boolean adminIsTeacher = RoleType.ADMIN == RoleType.TEACHER;
	}

}
```

</details>  


I left our `RoleType` enum as the most primitive version of a Java `enum` possible.

***NOTE**—For anyone interested in learning more about enums, the Java `enum` is actually incredibly capable and
powerful. (Far more so than enums in I've encountered in any other language.) If this sparks any curiosity whatsoever,
I'd totally encourage you to [learn more about them](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html)*

```java
/**
 * Enum representation of our RoleTypes.
 *
 * Each member of the enum will have 
 */
public enum RoleType {
	ADMIN,
	TEACHER,
	STUDENT
}
```

Some example code for how we can USE that `RoleType` enum

```java
import com.lambdaschool.oktafoundation.models.RoleType;


public class UsingEnumExample {

	public static void main(String[] args) {

		// this would happen in Seed Data
		User user      = new User();
		Role adminRole = roleService.findByName(RoleType.ADMIN.name());
		user.getRoles()
				.add(new UserRoles(user, adminRole));

		// But now look at how easy it is to check RoleType for a User!!
		// We can use regular If, Else If, Else Branching
		if (user.getRole() == RoleType.ADMIN) {
			System.out.println("User is an ADMIN!");
		} else if (user.getRole() == RoleType.TEACHER) {
			System.out.println("User is a TEACHER!");
		} else {
			System.out.println("User is a STUDENT!");
		}

		// Or, even cleaner, we can use Java's Switch Statement
		switch (user.getRole()) {
			case ADMIN:
				System.out.println("User is an ADMIN!");
				break;
			case TEACHER:
				System.out.println("User is an TEACHER!");
				break;
			case STUDENT:
				System.out.println("User is a STUDENT!");
				break;
			default:
				throw new IllegalArgumentException("Unknown role " + user.getRole() + ".");
				break;
		}
	}

} 
```

### `/models/User.java` — User Entity Class

```java

// package and imports excluded for brevity


@Entity
@Table(name = "users")
public class User {

	private long   userid;
	private String username;
	private String firstname;
	private String lastname;

	/**
	 * The role that this user holds. This drives permissions on what this user can and cannot do!
	 * 'RoleType' is simply an Enum class with three members: ADMIN, TEACHER, or STUDENT
	 */
	private RoleType roleType; // either ADMIN, TEACHER, or STUDENT

	/**
	 * Every Course that this user is attached to. 
	 * - STUDENT users would exist in "private Set<User> users" in Course.java
	 * - TEACHER users would exist in "private Set<User> users" in Course.java 
	 * - ADMIN users would be assigned as the `private User owner` in Course.java
	 */
	private Set<Course> courses = new HashSet<>();


	public User() {}

	// getters and setters
}

```

### `/models/Course.java` — Course Entity

```java

// package and imports excluded for brevity


@Entity
@Table(name = "courses")
public class Course {

	private long   courseid;
	private String coursename;
	private String coursedescription;

	/**
	 * The ADMIN user who "owns" this course, so to speak.
	 */
	private User owner;

	/**
	 * Every 'User' that belongs to this course.
	 * This HashSet will contain both TEACHER and STUDENT users 
	 */
	private Set<User> users = new HashSet<>();

	/**
	 * Every 'Module' that belongs to this course.
	 */
	private Set<Module> modules = new HashSet<>();


	public Course() {}

	// getters and setters

}
```

### `/models/Module.java` — Module Entity

```java

// package and imports excluded for brevity


@Entity
@Table(name = "modules")
public class Module {

	private long   moduleid;
	private String modulename;
	/**
	 * The Course that this Module belongs to
	 */
	private Course course;


	public Module() {}

	// getters and setters

}
```

--- 

## Quick Look at the Repo & Service Layer

One of the best parts of these model assemblers and the HATEOAS framework is the fact that it allows us to build AROUND
all of the same code we all know and love.

The repository layer doesn't change at all. Nor do the services. The model-assemblers are simply one additional layer
in-between the service-layer and the controller. But I will cover the details of how this works in the following
controller section.

I will attach each Service interface below in case it's helpful to see what the methods are. Don't worry about the
implementation, though — the `_ServiceImpl.java` & `_Repository.java` for each entity is unaffected by HATEOAS.

### UserService Interface

<details>

<summary>Unfold to See Code</summary>

```java
package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

	List<User> findAll();

	User findUserById(long userId);


	List<User> findByNameContaining(String username);

	User findByName(String name);


	User save(User user);

	User update(
			User user,
			long id
	);

	User updateRole(
			User user,
			RoleType roleType
	);

	void delete(long userId);
	void deleteAll();


}

```

</details>

### CourseService Interface

<details>

<summary>Unfold to see Code</summary>

```java
package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Course;

import java.util.List;


public interface CourseService {

	List<Course> findAll();

	List<Course> findRelevant(String query);

	List<Course> findByUser(long userid);

	Course findCourseById(long courseId);

	Course get(long courseid);
	Course get(String coursename);

	List<Course> findByTag(String tagTitle);

	Course save(Course course);
	Course save(
			Course course,
			long programid
	);

	Course save(
			long programId,
			Course course
	);

	Course update(
			long courseId,
			Course course
	);

	void delete(long courseId);


}

```

</details>

### ModuleService Interface

<details>

<summary>Unfold to see code</summary>

```java
package com.lambdaschool.oktafoundation.services;


import com.lambdaschool.oktafoundation.models.Module;

import java.util.List;


public interface ModuleService {

	List<Module> findAll();

	Module find(long moduleId);

	Module find(String name);


	Module findModulesById(long id);

	void replaceMarkdown(
			Long moduleid,
			String markdown
	);

	Module save(
			long id,
			Module module
	);

	Module update(
			long id,
			Module module
	);

	void delete(long id);

}

```

</details>

---

## Code for Controllers — Before Model Assemblers

In the past, all controller classes `@Autowire` in any necessary `Service` classes (or `interfaces`, rather) needed to
get each collection or view of data to send in a response body. This remains true in our implementation.

The "new" part is we're going to receive whatever the `Service` responds with, then we're going to transform that view
into a Relational Representation by UTILIZING our Model Assemblers.

Let's first look at what these controllers would look like without the Model Assemblers in place.

### `UserController.java` (Before Model Assembler)

<details>


<summary>Unfold to see code...</summary>


---

### A Simple User Controller without any HATEOAS

```java
package com.lambdaschool.oktafoundation.controllers;


import com.lambdaschool.oktafoundation.exceptions.RoleNotSufficientException;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import com.lambdaschool.oktafoundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HelperFunctions helperFunctions;

	// GET All Users
	@GetMapping(value = "/users", produces = "application/json")
	public ResponseEntity<?> listAllUsers() {
		List<User> users = userService.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	// GET User by userId
	@GetMapping(value = "/users/user/{userId}", produces = "application/json")
	public ResponseEntity<?> getUserById(
			@PathVariable
					Long userId
	) {
		User user = userService.findUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// CREATE new user
	@PostMapping(value = "/users/user", consumes = "application/json")
	public ResponseEntity<?> addNewUser(
			@Valid
			@RequestBody
					User newUser
	) {
		RoleType callingUserRole = helperFunctions.getCurrentPriorityRole();
		if (callingUserRole != RoleType.ADMIN) {
			throw new RoleNotSufficientException("Your role is not sufficient to create a new user");
		}
		newUser.setUserid(0);
		userService.save(newUser);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// REPLACE full existing user
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

	// EDIT/UPDATE partial existing user
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

	// REPLACE User's role
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
			userService.updateRole(userToEdit, roleType);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// DELETE a user
	@DeleteMapping(value = "/users/user/{userid}")
	public ResponseEntity<?> deleteUserById(
			@PathVariable
					long userid
	) {
		userService.delete(userid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}

```

</details>

### `CourseController.java` (Before Model Assembler)

[comment]: <> (<details>)

[comment]: <> (<summary>Unfold to see code...</summary>)

[comment]: <> (```java)

[comment]: <> (```)

[comment]: <> (</details>)

### `ModuleController.java` (Before Model Assembler)

[comment]: <> (<details>)

[comment]: <> (<summary>Unfold to see code...</summary>)

[comment]: <> (```java)

[comment]: <> (```)

[comment]: <> (</details>)

---

## Code for Model Assemblers

### `UserModelAssembler.java`

<details>

<summary>Unfold to see code...</summary>

```java
package com.lambdaschool.oktafoundation.modelAssemblers;


import com.lambdaschool.oktafoundation.controllers.CourseController;
import com.lambdaschool.oktafoundation.controllers.ProgramController;
import com.lambdaschool.oktafoundation.controllers.UserController;
import com.lambdaschool.oktafoundation.models.RoleType;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler
		implements RepresentationModelAssembler<User, EntityModel<User>> {

	@Autowired
	HelperFunctions helperFunctions;

	@Override
	public EntityModel<User> toModel(User user) {
		EntityModel<User> userEntityModel = EntityModel.of(user,
				// Link to SELF --- GET /users/user/{userid}
				linkTo(methodOn(UserController.class).getUserById(user.getUserid())).withSelfRel(),
				// Link to self by name --- GET /users/user/name/{username}
				linkTo(methodOn(UserController.class).getUserByName(user.getUsername())).withRel("self_by_name")
		);

		// this will hold the role of the CALLING USER -- whomever is hitting this endpoint should have a role
		RoleType callingUser = helperFunctions.getCurrentPriorityRole();

		// this will hold the role of the user to be converted into a model
		RoleType usersRole = user.getRole();

		// if the user to convert to a model is a STUDENT, add the following links
		if (usersRole == RoleType.STUDENT) {
			userEntityModel.add(linkTo(methodOn(CourseController.class).getStudentCourses(user.getUserid())).withRel("courses"));

			if (callingUser == RoleType.ADMIN) {
				userEntityModel.add(linkTo(methodOn(CourseController.class).getUserAntiCourses(user.getUserid())).withRel(
						"available_courses"),
						linkTo(methodOn(CourseController.class).getMappifiedCoursesByUser(user.getUserid())).withRel(
								"mappified_courses")
				);
			}
		}

		// if the user to convert to a model is a TEACHER, add the following links
		if (usersRole == RoleType.TEACHER) {
			userEntityModel.add(linkTo(methodOn(CourseController.class).getTeacherCourses(user.getUserid())).withRel("courses"));
			if (callingUser == RoleType.ADMIN) {
				userEntityModel.add(linkTo(methodOn(CourseController.class).getUserAntiCourses(user.getUserid())).withRel(
						"available_courses"),
						linkTo(methodOn(CourseController.class).getMappifiedCoursesByUser(user.getUserid())).withRel(
								"mappified_courses")
				);
			}
		}

		// if the user to convert to a model is an ADMIN, add the following links
		if (usersRole == RoleType.ADMIN) {
			// Link to GET Programs by User.userid
			userEntityModel.add( //
					linkTo(methodOn(ProgramController.class).getProgramsByUserId(user.getUserid())).withRel("programs"));
		}

		// if the calling user is an admin and the user in question is NOT an admin
		if (callingUser == RoleType.ADMIN && user.getRole() != RoleType.ADMIN) {
			userEntityModel.add(
					// Link to DELETE User by User.userid
					linkTo(methodOn(UserController.class).deleteUserById(user.getUserid())).withRel("delete_user"),
					// Link to PUT "/users/user/{userid}"
					linkTo(methodOn(UserController.class).updateFullUser(null, user.getUserid())).withRel("replace_user"),
					// Link to PATCH "/users/user/{userid}"
					linkTo(methodOn(UserController.class).updateUser(null, user.getUserid())).withRel("edit_user"),
					// Link to PATCH "/users/user/{userid}/STUDENT"
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.STUDENT)).withRel(
							"make_student"),
					// Link to PATCH "/users/user/{userid}/ADMIN"
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.ADMIN)).withRel("make_admin"),
					// Link to PATCH "/users/user/{userid}/TEACHER"
					linkTo(methodOn(UserController.class).updateUserRole(user.getUserid(), RoleType.TEACHER)).withRel(
							"make_teacher")
			);
		}

		return userEntityModel;
	}

}

```

</details>

---

## Code for Controllers -- After Model Assembler

### `UserController.java` (After Model Assembler)

<details>

<summary>Unfold to see code...</summary>

--- 

### What do we have to change in our User Controller to use this assembler?

So, realistically, the only purpose of any Model Assembler is to format our entities in this fancy, relational way. The
only time we ever need to DO that is when we're sending information in the body of our
`ResponseEntity`. For the sake of this tutorial, we only ever sent data in GET Requests (`@GetMapping`).

The good news is that our `UserController` currently has only two `GET` endpoints.

> All that changes in our controller classes is that we will USE the `_ModelAssembler` classes to transform whatever our `_Service` classes give us into hypermedia-rich views before sending that data to the client.



First things first — let's import the various classes and methods we need to use the Assembler. Here are the new imports
that we need to add into the mix:

```java
package com.lambdaschool.oktafoundation.controllers;

// repeated imports excluded

import com.lambdaschool.oktafoundation.modelAssemblers.UserModelAssembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
```

Perfect! Now we need to find each `GET` request and transform any plain data models into EntityModel.

In the `UserController` class, we only have two GET requests! One of which is a `GET user by userid` and the other is
a `GET ALL users`. What's the big difference here? One is a single entity; the other is a COLLECTION of entities.

```java

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	// We Autowire in the UserModelAssembler so that we can use it!! 
	@Autowired
	private UserModelAssembler userModelAssembler;

	@Autowired
	private HelperFunctions helperFunctions;

	// GET All Users
	@GetMapping(value = "/users", produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<User>>> listAllUsers() {
		List<User> users = userService.findAll(); // this is what we used to put in our response body

		// we need to take our List<User> that was returned from userService.findAll()' and 
		// transform each user into a EntityModel<User> 
		// .stream() is one of the most frequently used methods for functional-style operations
		//
		// the line of code below is about as close to JavaScript's array.map(some_fn) as you could hit in Java
		List<EntityModel<User>> userEntities = userService.findAll()
				.stream() // start stream
				.map(userModelAssembler::toModel) // calls userModelAssembler.toModel(User) for each entity in the stream
				// .map() actually returns a NEW Stream. So we need to collect that stream and turn it into a List
				.collect(Collectors.toList()); // collects our stream into a List

		// now that we've made our List<User> into a List<EntityModel<User>>, we can 
		// make a CollectionModel<EntityModel<User>>. 
		// This is how HATEOAS allows us to display a LIST or COLLECTION of those RESTful entities.
		//
		// CollectionModel.of(__COLLECTION__, __LINKS___) will take in a collection and any links you want to attach!
		// It's almost exactly the same as our 'toModel' function except for the fact that it takes in a 
		// COLLECTION of EntityModel<Data> instead of a singular Data.
		// 
		// The response body will have two properties: '_embedded' and '_links'.
		// _embedded will store the collection of entities 
		// _links will store any links we want to store FOR THE COLLECTION CALL.
		// note: each EntityModel<User> inside of the '_embedded' collection will STILL HAVE ALL OF ITS REPRESENTATIONAL DATA 
		CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(userEntities,
				linkTo(methodOn(UserController.class).listAllUsers(query)).withSelfRel()
		);

		// return the CollectionModel<EntityModel<User>> as the body of the response!! 
		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	// GET User by userId
	@GetMapping(value = "/users/user/{userId}", produces = "application/json")
	public ResponseEntity<EntityModel<User>> getUserById(
			@PathVariable
					Long userId
	) {
		// our regular old User entity
		User user = userService.findUserById(userId);

		// transform that User entity into an EntityModel with our assembler
		EntityModel<User> entityModel = userModelAssembler.toModel(user);

		// return the EntityModel<User> as the body instead of the regular old User
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	// All other operations did not change
	// All other operations did not change
	// All other operations did not change
}

```

</details>


---

# Resources

- Spring HATEOAS (v1.2.4)
    - [Reference Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/reference/html/#reference)
    - [API Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/api/)
- Spring REST Tutorial
    - [From the Beginning](https://spring.io/guides/tutorials/rest)
    - [New Content — What makes something RESTful?](https://spring.io/guides/tutorials/rest/#_what_makes_something_restful)
    - [Conditional Links — HATEOAS Section](https://spring.io/guides/tutorials/rest/#_building_links_into_your_rest_api)
