package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "program")
public class ProgramTags
extends Auditable
		implements Serializable {

	@EmbeddedId
	private ProgramTagsId programTagsId;

	@ManyToOne
//			(fetch = FetchType.LAZY)
	@MapsId("programid")
	@JsonIgnoreProperties(value = "tags", allowSetters = true)
	private Program program;

	@ManyToOne
//			(fetch = FetchType.LAZY)
	@MapsId("tagid")
	@JsonIgnoreProperties(value = "programs", allowSetters = true)
	private Tag tag;

	public ProgramTags() {}

	public ProgramTags(
			Program program,
			Tag tag
	) {
		this.programTagsId = new ProgramTagsId(program.getProgramid(), tag.getTagid());
		this.program       = program;
		this.tag           = tag;
	}

	public ProgramTagsId getProgramTagsId() {
		return programTagsId;
	}

	public void setProgramTagsId(ProgramTagsId programTagsId) {
		this.programTagsId = programTagsId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProgramTags that = (ProgramTags) o;
		return Objects.equals(getProgramTagsId(), that.getProgramTagsId()) &&
		       Objects.equals(getProgram(), that.getProgram()) && Objects.equals(getTag(), that.getTag());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProgramTagsId(), getProgram(), getTag());
	}

	@Override
	public String toString() {
		return "ProgramTags{" + "programTagsId=" + programTagsId + ", program=" + program + ", tag=" + tag + '}';
	}

}
