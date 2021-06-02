package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "userCourses")
@IdClass(UserCoursesId.class)
public class UserCourses
		extends Auditable
		implements Serializable {

	@Id
	@ManyToOne
	@NotNull
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties(value = "courses", allowSetters = true)
	private User user;

	@Id
	@ManyToOne
	@NotNull
	@JoinColumn(name = "courseId")
	@JsonIgnoreProperties(value = "users", allowSetters = true)
	private Course course;

	public UserCourses() {
	}

	public UserCourses(
			User user,
			Course course
	) {
		this.user   = user;
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public boolean softEquals(User user, Course course) {
		return this.user.equals(user) && this.course.equals(course);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, course);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserCourses)) {
			return false;
		}
		UserCourses that = (UserCourses) o;
		return ((user == null) ? 0 : user.getUserId()) == ((that.user == null) ? 0 : that.user.getUserId()) &&
		       ((course == null) ? 0 : course.getCourseId()) == ((that.course == null) ? 0 : that.course.getCourseId());
	}

	@Override
	public String toString() {
		return "UserCourses{" + "user=" + user.getUserId() + ", course=" + course.getCourseId() + '}';
	}

}
