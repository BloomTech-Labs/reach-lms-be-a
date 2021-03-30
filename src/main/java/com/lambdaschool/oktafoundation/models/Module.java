package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity
@Table(name = "modules")
@JsonIgnoreProperties(value = {"course", "modulecontent"}, allowSetters = true)
public class Module
		extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long moduleid;

	@Column(nullable = false)
	private String modulename;

	@Size(max=2000)
	private String moduledescription;

	@Column(nullable = false)
	@Size(max=20000)
	private String modulecontent;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "courseid", nullable = false)
	@JsonIgnoreProperties(value = "modules")
	private Course course;

	public Module() {
	}

	public Module(
			String modulename,
			String moduledescription,
			String modulecontent,
			Course course
	) {
		this.modulename = modulename;
		this.moduledescription = moduledescription;
		this.modulecontent = modulecontent;
		this.course = course;
	}

	public long getModuleid() {
		return moduleid;
	}

	public void setModuleid(long moduleid) {
		this.moduleid = moduleid;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	public String getModuledescription() {
		return moduledescription;
	}

	public void setModuledescription(String moduledescription) {
		this.moduledescription = moduledescription;
	}

	public String getModulecontent() {
		return modulecontent;
	}

	public void setModulecontent(String modulecontent) {
		this.modulecontent = modulecontent;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getModuleid(), getModulename(), getModuledescription(), getModulecontent(), getCourse());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Module module = (Module) o;
		return getModuleid() == module.getModuleid() && Objects.equals(getModulename(), module.getModulename()) &&
		       Objects.equals(getModuledescription(), module.getModuledescription()) &&
		       Objects.equals(getModulecontent(), module.getModulecontent()) &&
		       Objects.equals(getCourse(), module.getCourse());
	}

	@Override
	public String toString() {
		return "Module{" + "moduleid=" + moduleid + ", modulename='" + modulename + '\'' + ", moduledescription='" +
		       moduledescription + '\'' + ", modulecontent='" + modulecontent + '\'' + ", course=" + course + '}';
	}

}
