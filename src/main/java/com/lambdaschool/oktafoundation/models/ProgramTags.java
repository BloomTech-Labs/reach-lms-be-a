package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "programtags")
@JsonIgnoreProperties(value = {"id", "program"})
public class ProgramTags
		extends Auditable
		implements Serializable {

	@EmbeddedId
	@Column(name = "program_tag_id")
	private ProgramTagsId id;
	//
	@ManyToOne
	@MapsId("programid")
	@JsonIgnoreProperties(value = "tags", allowSetters = true)
	private Program       program;
	//
	@ManyToOne
	@MapsId("tagid")
	@JsonIgnoreProperties(value = "programs", allowSetters = true)
	private Tag           tag;

	public ProgramTags() {}

	public ProgramTags(
			Program program,
			Tag tag
	) {
		this.id      = new ProgramTagsId(program.getProgramid(), tag.getTagid());
		this.program = program;
		this.tag     = tag;
	}

	public ProgramTagsId getId() {
		return id;
	}

	public void setId(ProgramTagsId id) {
		this.id = id;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public boolean softEquals(
			Program program,
			Tag tag
	) {
		return this.program.equals(program) && this.tag.equals(tag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getProgram(), getTag());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProgramTags that = (ProgramTags) o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getProgram(), that.getProgram()) &&
		       Objects.equals(getTag(), that.getTag());
	}

	@Override
	public String toString() {
		return "ProgramTags{" + "id=" + id + ", program=" + program.getProgramid() + ", tag=" + tag + '}';
	}

}
