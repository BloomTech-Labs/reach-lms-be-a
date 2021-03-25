package com.lambdaschool.oktafoundation.models;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class UserCoursesId
		implements Serializable {

	private long course;
	private long user;

	public UserCoursesId() {
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getCourse() {
		return course;
	}

	public void setCourse(long course) {
		this.course = course;
	}

	@Override
	public int hashCode() {return Objects.hash(user, course);}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserCoursesId that = (UserCoursesId) o;
		return user == that.user && course == that.course;
	}

	@Override
	public String toString() {
		return "UserCoursesId{" + "course=" + course + ", user=" + user + '}';
	}

}
