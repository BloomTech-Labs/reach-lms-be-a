package com.lambdaschool.oktafoundation.models;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class ProgramTagsId
		implements Serializable {

//	@Column(name = "programId")
	private Long programId;

//	@Column(name = "tagId")
	private Long tagId;

	public ProgramTagsId() {}

	public ProgramTagsId(
			Long programId,
			Long tagId
	) {
		this.programId = programId;
		this.tagId     = tagId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProgramId(), getTagId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProgramTagsId that = (ProgramTagsId) o;
		return Objects.equals(getProgramId(), that.getProgramId()) && Objects.equals(getTagId(), that.getTagId());
	}

	@Override
	public String toString() {
		return "ProgramTagsId{" + "programId=" + programId + ", tagId=" + tagId + '}';
	}

}
