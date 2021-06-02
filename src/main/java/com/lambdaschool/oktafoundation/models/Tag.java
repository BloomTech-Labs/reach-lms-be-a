package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "tags")
@JsonIgnoreProperties(value = "courses")
public class Tag
		extends Auditable {

	private static final String           DEFAULT_HEX_CODE = "#FFFFFF";
	//
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private              long             tagId;
	//
	@NotNull
	private              String           title;
	//
	@NotNull
	private              String           hexCode;
	//
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private              Set<ProgramTags> programs         = new HashSet<>();
	//
	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "course_id")
	@JsonIgnoreProperties(value = "tags")
	private              Set<ProgramTags> courses          = new HashSet<>();

	public Tag() {}

	public Tag(
			@NotNull String title,
			@NotNull String hexCode
	) {
		this.title   = title;
		this.hexCode = hexCode;
	}

	public Tag(@NotNull String title) {
		this.title   = title;
		this.hexCode = DEFAULT_HEX_CODE;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHexCode() {
		return hexCode;
	}

	public void setHexCode(String hexCode) {
		this.hexCode = hexCode;
	}

	public Set<ProgramTags> getPrograms() {
		return programs;
	}

	public void setPrograms(Set<ProgramTags> programs) {
		this.programs = programs;
	}

	public Set<ProgramTags> getCourses() {
		return courses;
	}

	public void setCourses(Set<ProgramTags> courses) {
		this.courses = courses;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTagId(), getTitle());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Tag tag = (Tag) o;
		return getTagId() == tag.getTagId() && getTitle().equals(tag.getTitle());
	}

	@Override
	public String toString() {
		return "Tag{" + "tagId=" + tagId + ", title='" + title + '\'' + ", hexCode='" + hexCode + '\'' + '}';
	}

}
