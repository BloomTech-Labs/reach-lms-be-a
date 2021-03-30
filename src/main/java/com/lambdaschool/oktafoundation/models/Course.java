package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lambdaschool.oktafoundation.exceptions.UserNotFoundException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Table(name = "courses")
@JsonIgnoreProperties(value = {"program", "users", "modules"}, allowSetters = true)
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long             courseid;
	//
	@NotNull
	private String           coursename;
	//
	@NotNull
	private String           coursecode;
	//
	@Size(max = 2000)
	private String           coursedescription;
	//
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "programid")
	@JsonIgnoreProperties(value = "courses")
	private Program          program;
	//
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties(value = "course", allowSetters = true)
	private Set<UserCourses> users   = new HashSet<>();
	//
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties(value = "course", allowSetters = true)
	private Set<Module>      modules = new HashSet<>();
	//
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumns({
			@JoinColumn(name = "program_programid"), @JoinColumn(name = "tag_tagid")
	})
	@JsonIgnoreProperties(value = {"program"})
	private ProgramTags      tag;

	public Course() {
	}

	public Course(
			@NotNull String coursename,
			@NotNull String coursecode,
			String coursedescription,
			Program program
	) {
		this.coursename        = coursename;
		this.coursecode        = coursecode;
		this.coursedescription = coursedescription;
		this.program           = program;
	}

	public long getCourseid() {
		return courseid;
	}

	public void setCourseid(long courseid) {
		this.courseid = courseid;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}

	public String getCoursedescription() {
		return coursedescription;
	}

	public void setCoursedescription(String coursedescription) {
		this.coursedescription = coursedescription;
	}

	@JsonIgnore // JsonIgnore as to not serialize this to JSON
	public ProgramTags getTag() {
		return tag;
	}

	// Regular Setter for setTag
	public void setTag(ProgramTags tag) {
		ProgramTags newTag = program.getTagIfPresent(tag.getTag()); // throws if no such tag in program
		this.tag = newTag;
	}

	// Overload for ease of use -- this version accepts a regular Tag
	public void setTag(Tag tag) {
		ProgramTags newTag = program.getTagIfPresent(tag); // throws if no such tag in program
		setTag(newTag);
	}

	/**
	 * Returns the tag associated with this course in the specific way that we want it to.
	 * <p>
	 * Note that "coursetype" is not an ACTUAL property on this entity.
	 * However, adding a simple getter like this will allow Jackson to serialize this data!
	 * <p>
	 *
	 * @return The {@link Tag tag} associated with this course if present, else just return null
	 */
	public Tag getCoursetype() {
		if (tag != null) {
			return tag.getTag();
		} else {
			return null;
		}
	}

	public void removeTag(Tag tag) {
		program.getTagIfPresent(tag); // throws if no such tag in program
		this.tag = null;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Set<UserCourses> getUsers() {
		return users;
	}

	public void setUsers(Set<UserCourses> users) {
		this.users = users;
	}

	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	public Optional<UserCourses> findUser(User user) {
		for (UserCourses userCourses : users) {
			if (userCourses.softEquals(user, this)) {
				return Optional.of(userCourses);
			}
		}
		return Optional.empty();
	}

	public boolean containsUser(User user) {
		return findUser(user).isPresent();
	}

	public UserCourses getUserIfPresent(User user)
	throws UserNotFoundException {
		return findUser(user).orElseThrow(() -> new UserNotFoundException(user.getUserid(), this));
	}

	public void addUser(UserCourses userCourse) {
		addUser(userCourse.getUser());
	}

	public void addUser(List<User> users) {
		for (User user : users) {
			addUser(user);
		}
	}

	public void addUser(User user) {
		UserCourses userCourse = new UserCourses(user, this);
		users.add(userCourse);
	}

	public void removeUser(UserCourses userCourse) {
		removeUser(userCourse.getUser());
	}

	public void removeUser(User user) {
		if (!containsUser(user)) {
			throw new UserNotFoundException(user.getUserid(), this);
		}
		Iterator<UserCourses> iterator = users.iterator();
		while (iterator.hasNext()) {
			UserCourses userCourses = iterator.next();
			if (userCourses.getCourse()
					    .equals(this) && userCourses.getUser()
					    .equals(user)) {
				iterator.remove();
				userCourses.getUser()
						.getCourses()
						.remove(userCourses);
				userCourses.setCourse(null);
				userCourses.setUser(null);
			}
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCourseid(), getCoursename(), getCoursecode(), getCoursedescription());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Course course = (Course) o;
		return getCourseid() == course.getCourseid() && getCoursename().equals(course.getCoursename()) &&
		       Objects.equals(getCoursecode(), course.getCoursecode()) &&
		       Objects.equals(getCoursedescription(), course.getCoursedescription());
	}

}
