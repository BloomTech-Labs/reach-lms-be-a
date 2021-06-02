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
	private long moduleId;

	@Column(nullable = false)
	private String moduleName;

	@Size(max=2000)
	private String moduleDescription;

	@Column(nullable = false)
	@Size(max=20000)
	private String moduleContent;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "courseid", nullable = false)
	@JsonIgnoreProperties(value = "modules")
	private Course course;

	public Module() {
	}

	public Module(
			String moduleName,
			String moduleDescription,
			String moduleContent,
			Course course
	) {
		this.moduleName        = moduleName;
		this.moduleDescription = moduleDescription;
		this.moduleContent     = moduleContent;
		this.course            = course;
	}

	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleid) {
		this.moduleId = moduleid;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String modulename) {
		this.moduleName = modulename;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(String moduledescription) {
		this.moduleDescription = moduledescription;
	}

	public String getModuleContent() {
		return moduleContent;
	}

	public void setModuleContent(String modulecontent) {
		this.moduleContent = modulecontent;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getModuleId(), getModuleName(), getModuleDescription(), getModuleContent(), getCourse());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Module module = (Module) o;
		return getModuleId() == module.getModuleId() && Objects.equals(getModuleName(), module.getModuleName()) &&
		       Objects.equals(getModuleDescription(), module.getModuleDescription()) &&
		       Objects.equals(getModuleContent(), module.getModuleContent()) &&
		       Objects.equals(getCourse(), module.getCourse());
	}

	@Override
	public String toString() {
		return "Module{" + "moduleid=" + moduleId + ", modulename='" + moduleName + '\'' + ", moduledescription='" +
		       moduleDescription + '\'' + ", modulecontent='" + moduleContent + '\'' + ", course=" + course + '}';
	}

}
