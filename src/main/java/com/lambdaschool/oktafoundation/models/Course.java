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
	private long             courseId;
	//
	@NotNull
	private String           courseName;
	//
	@NotNull
	private String           courseCode;
	//
	@Size(max = 2000)
	private String           courseDescription;
	//
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "programId")
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
			@JoinColumn(name = "program_programId"), @JoinColumn(name = "tag_tagId")
	})
	@JsonIgnoreProperties(value = {"program"})
	private ProgramTags      tag;

	public Course() {
	}

	public Course(
			@NotNull String courseName,
			@NotNull String courseCode,
			String courseDescription,
			Program program
	) {
		this.courseName        = courseName;
		this.courseCode        = courseCode;
		this.courseDescription = courseDescription;
		this.program           = program;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}


	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}


	//	// supporting all lowers for reverse compatibility
	//	public long getCourseid() {
	//		return getCourseId();
	//	}
	//
	//	// supporting all lowers for reverse compatibility
	//	public String getCoursename() {
	//		return getCourseName();
	//	}
	//
	//	// supporting all lowers for reverse compatibility
	//	public String getCoursedescription() {
	//		return getCourseDescription();
	//	}
	//
	//	// supporting all lowers for reverse compatibility
	//	public String getCoursecode() {
	//		return getCourseCode();
	//	}
	//
	//	// supporting all lowers for reverse compatibility
	//	public Tag getCoursetype() {
	//		return getCourseType();
	//	}

	@JsonIgnore // JsonIgnore as to not serialize this to JSON
	public ProgramTags getTag() {
		return tag;
	}

	// Regular Setter for setTag
	public void setTag(ProgramTags tag) {
		this.tag = program.getTagIfPresent(tag.getTag());
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
	public Tag getCourseType() {
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
		return findUser(user).orElseThrow(() -> new UserNotFoundException(user.getUserId(), this));
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
			throw new UserNotFoundException(user.getUserId(), this);
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
		return Objects.hash(getCourseId(), getCourseName(), getCourseCode(), getCourseDescription());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Course course = (Course) o;
		return getCourseId() == course.getCourseId() && getCourseName().equals(course.getCourseName()) &&
		       Objects.equals(getCourseCode(), course.getCourseCode()) &&
		       Objects.equals(getCourseDescription(), course.getCourseDescription());
	}


}
