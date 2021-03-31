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

## Basic Example

I want to quickly run through an example of how the

```json
// Templated Endpoint --- GET "/entities/entity/{entityId}"
// Actual Endpoint called --- GET "/entities/entity/4"
{
  "entityId": 4,
  "entityName": "This is just an example of a plain entity!",
  "entityType": "POJO",
  "roles": [
    {
      "roleName": "ROLE_ONE",
      "roleDescription": "Uh oh... this is nested data... what if this array was 100 objects long?"
    }
  ]
}
```

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
		implements RepresentationModelAssembler<E, EntityModel<E>> {

	@Override
	public EntityModel<E> toModel(E entity) {
		// this method will take in some POJO entity and 
		// transform it into our EntityModel representation

		EntityModel<E> entityModel = EntityModel.of(entity,
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
				// line of code will attach a PROPERTY within the '_links' object with a 
				linkTo(methodOn(ExampleController.class).getEntityByEntityId(entity.getId())).withSelfRel()
		);

		if (entity.has)

			return entityModel;

	}

}
```

## Resources

- Spring HATEOAS (v1.2.4)
    - [Reference Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/reference/html/#reference)
    - [API Docs](https://docs.spring.io/spring-hateoas/docs/1.2.4/api/)
- Spring REST Tutorial
    - [From the Beginning](https://spring.io/guides/tutorials/rest/)
    - [New Content — What makes something RESTful?](https://spring.io/guides/tutorials/rest/#_what_makes_something_restful)
    - [Conditional Links — HATEOAS Section](https://spring.io/guides/tutorials/rest/#_building_links_into_your_rest_api)
