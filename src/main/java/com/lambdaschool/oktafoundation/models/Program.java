package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lambdaschool.oktafoundation.exceptions.TagNotFoundException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "programs")
@JsonIgnoreProperties(value = {"courses", "user", "tags"})
public class Program
		extends Auditable {


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties(value = "program", allowSetters = true)
	List<Course> courses = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long             programid;
	private String           programname;
	private String           programtype;
	private String           programdescription;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid", nullable = false)
	@JsonIgnoreProperties(value = "programs")
	private User             user;
	@OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = "program")
	private Set<ProgramTags> tags = new HashSet<>();

	public Program() {
	}

	public Program(
			String programname,
			String programtype,
			String programdescription
	) {
		this.programname        = programname;
		this.programtype        = programtype;
		this.programdescription = programdescription;
	}

	public long getProgramid() {
		return programid;
	}

	public void setProgramid(long programid) {
		this.programid = programid;
	}

	public String getProgramname() {
		return programname;
	}

	public void setProgramname(String programname) {
		this.programname = programname;
	}

	public String getProgramtype() {
		return programtype;
	}

	public void setProgramtype(String programtype) {
		this.programtype = programtype;
	}

	public String getProgramdescription() {
		return programdescription;
	}

	public void setProgramdescription(String programdescription) {
		this.programdescription = programdescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Set<ProgramTags> getTags() {
		return tags;
	}

	public void setTags(Set<ProgramTags> tags) {
		this.tags = tags;
	}

	public void addTag(ProgramTags programTags) {
		addTag(programTags.getTag());
	}

	public void addTag(Tag tag) {
		ProgramTags programTag = new ProgramTags(this, tag);
		tags.add(programTag);
	}

	public void removeTag(ProgramTags programTags) {
		removeTag(programTags.getTag());
	}

	public void removeTag(Tag tag) {
		if (!containsTag(tag)) {
			throw new TagNotFoundException(tag.getTitle(), programname);
		}
		Iterator<ProgramTags> it = tags.iterator();
		while (it.hasNext()) {
			ProgramTags programTag = it.next();
			if (programTag.getProgram()
					    .equals(this) && programTag.getTag()
					    .equals(tag)) {
				it.remove();
				programTag.setProgram(null);
				programTag.setTag(null);
			}
		}
	}

	public boolean containsTag(Tag tag) {
		return findTag(tag).isPresent();
	}

	public Optional<ProgramTags> findTag(Tag tag) {
		for (ProgramTags programTags : tags) {
			if (programTags.softEquals(this, tag)) {
				return Optional.of(programTags);
			}
		}
		return Optional.empty();
	}

	public ProgramTags getTagIfPresent(Tag tag)
	throws TagNotFoundException {
		return findTag(tag).orElseThrow(() -> new TagNotFoundException(tag.getTitle(), programname));
	}

}

