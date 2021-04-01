# Representational Model Assemblers

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


--- 

# Resources

- Spring HATEOAS (v1.2.4)
    - [Reference Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/reference/html/#reference)
    - [API Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/api/)
- Spring REST Tutorial
    - [From the Beginning](https://spring.io/guides/tutorials/rest/)
    - [New Content — What makes something RESTful?](https://spring.io/guides/tutorials/rest/#_what_makes_something_restful)
    - [Conditional Links — HATEOAS Section](https://spring.io/guides/tutorials/rest/#_building_links_into_your_rest_api)
