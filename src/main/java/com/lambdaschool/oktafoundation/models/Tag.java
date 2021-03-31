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
	private              long             tagid;
	//
	@NotNull
	private              String           title;
	//
	@NotNull
	private              String           hexcode;
	//
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private              Set<ProgramTags> programs         = new HashSet<>();
	//
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "courseid")
	@JsonIgnoreProperties(value = "tags")
	private              Set<ProgramTags> courses          = new HashSet<>();

	public Tag() {}

	public Tag(
			@NotNull String title,
			@NotNull String hexcode
	) {
		this.title   = title;
		this.hexcode = hexcode;
	}

	public Tag(@NotNull String title) {
		this.title   = title;
		this.hexcode = DEFAULT_HEX_CODE;
	}

	public long getTagid() {
		return tagid;
	}

	public void setTagid(long tagId) {
		this.tagid = tagId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHexcode() {
		return hexcode;
	}

	public void setHexcode(String hexCode) {
		this.hexcode = hexCode;
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
		return Objects.hash(getTagid(), getTitle());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Tag tag = (Tag) o;
		return getTagid() == tag.getTagid() && getTitle().equals(tag.getTitle());
	}

	@Override
	public String toString() {
		return "Tag{" + "tagId=" + tagid + ", title='" + title + '\'' + ", hexCode='" + hexcode + '\'' + '}';
	}

}
