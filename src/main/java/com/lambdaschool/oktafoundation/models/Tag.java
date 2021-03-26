package com.lambdaschool.oktafoundation.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Entity
@Table(name = "tags")
public class Tag
		extends Auditable {

	private static final String DEFAULT_HEX_CODE = "#000000";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long   tagid;
	@NotNull
	private String title;
	@NotNull
	private String hexcode;

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
