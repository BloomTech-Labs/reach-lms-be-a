package com.lambdaschool.oktafoundation.models;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class ProgramTagsId implements Serializable {
	@Column(name="programid")
	private Long programid;
	@Column(name="tagid")
	private Long tagid;

	public ProgramTagsId() {}

	public ProgramTagsId(
			Long programid,
			Long tagid
	) {
		this.programid = programid;
		this.tagid     = tagid;
	}

	public Long getProgramid() {
		return programid;
	}

	public void setProgramid(Long programid) {
		this.programid = programid;
	}

	public Long getTagid() {
		return tagid;
	}

	public void setTagid(Long tagid) {
		this.tagid = tagid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProgramTagsId that = (ProgramTagsId) o;
		return Objects.equals(getProgramid(), that.getProgramid()) && Objects.equals(getTagid(), that.getTagid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProgramid(), getTagid());
	}

	@Override
	public String toString() {
		return "ProgramTagsId{" + "programid=" + programid + ", tagid=" + tagid + '}';
	}

}
