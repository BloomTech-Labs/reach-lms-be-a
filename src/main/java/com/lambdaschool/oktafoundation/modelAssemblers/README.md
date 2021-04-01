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

- [Resources](#resources)

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
- Attach User to Course
    - `PUT "/users/user/{userid}/course/{courseid}"`
- Detach User from Course
    - `DELETE "/users/user/{userid}/course/{courseid}"`
- Get all Users associated with a Course
    - `GET "/users/enrolled-in/{courseid}"`
- Get any users NOT associated with a Course
    - `GET "/users/not-enrolled-in/{courseid}"`

### Course Controller

- `GET "/courses"`
- `GET "/courses/course/{courseid}"`
- `GET "/courses/course/name/{coursename}"`
- `POST "/courses/course"`
- `PUT "/courses/course/{courseid}"`
- `PATCH "/courses/course/{courseid}"`
- `DELETE "/courses/course/{courseid}"`

### Module Controller

- `GET "/modules"`
- `GET "/modules/module/{moduleid}`
- `GET "/modules/by-course/{courseid}`
- `POST "modules/to-course/{courseid}"`
- `DELETE "/modules/module/{moduleid}`

## Code for Models

Now that we know a bit more about what we're building, let's look at the entity class for each model. This is the same
goodness that you all have seen since the first week of Unit 4

### `/models/RoleType.java` — RoleType Enum

I know some of you aren't going to be familiar with Enums. For anone unfamiliar with the concept of enums, they are
simply a special data type allows for pre-defined groups of constants.

This is really convenient for little collections of data that (1.) don't change throughout our application and (2.) are
referenced a lot. That makes the `enum` a PERFECT choice for our user's `role`!!!

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

# Resources

- Spring HATEOAS (v1.2.4)
    - [Reference Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/reference/html/#reference)
    - [API Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/api/)
- Spring REST Tutorial
    - [From the Beginning](https://spring.io/guides/tutorials/rest/)
    - [New Content — What makes something RESTful?](https://spring.io/guides/tutorials/rest/#_what_makes_something_restful)
    - [Conditional Links — HATEOAS Section](https://spring.io/guides/tutorials/rest/#_building_links_into_your_rest_api)
