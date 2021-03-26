package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "courses")
@JsonIgnoreProperties(value = {"program", "users", "modules"}, allowSetters = true)
public class Course {

	@ManyToMany(cascade = {CascadeType.ALL})
//	name = "course_tags", joinColumns = {@JoinColumn(name = "programid")},
	//			inverseJoinColumns = {@JoinColumn(name = "program_tag_id")}
	@JoinColumn(name="program_tag_id")
//	@JoinColumns({
//			@JoinColumn(name="program_tag_id"),
////			@JoinColumn(name="programid")
//	})
	@JsonIgnoreProperties(value = "program")
	Set<ProgramTags> tags = new HashSet<>();
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long             courseid;
	@NotNull
	private String           coursename;
	@NotNull
	private String           coursecode;
	private String           coursedescription;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "programid")
	@JsonIgnoreProperties(value = "courses")
	private Program          program;
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties(value = "course", allowSetters = true)
	private Set<UserCourses> users   = new HashSet<>();
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties(value = "course", allowSetters = true)
	private Set<Module>      modules = new HashSet<>();

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

	public Set<ProgramTags> getTags() {
		return tags;
	}

	public void setTags(Set<ProgramTags> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		ProgramTags newTag = program.getTagIfPresent(tag);
		tags.add(newTag);
	}

	public void removeTag(Tag tag) {
		ProgramTags tagToRemove = program.getTagIfPresent(tag);
		tags.remove(tagToRemove);
	}

	public void removeUser(User user) {
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
