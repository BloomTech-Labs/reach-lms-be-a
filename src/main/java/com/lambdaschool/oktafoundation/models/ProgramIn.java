package com.lambdaschool.oktafoundation.models;


import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class ProgramIn {

	private long programid;

	private String programname;

	private String programtype;

	@Size(max = 2000)
	private String programdescription;

	@Transient
	private boolean hasProgramid = false;

	private List<Tag> tags = new ArrayList<>();

	public ProgramIn() {}

	public ProgramIn(
			long programid,
			String programname,
			String programtype,
			@Size(max = 2000) String programdescription
	) {
		this.programid          = programid;
		this.hasProgramid       = true;
		this.programname        = programname;
		this.programtype        = programtype;
		this.programdescription = programdescription;
	}

	public Program toProgram(Program existingProgram) {
		if (hasProgramid) {
			existingProgram.setProgramid(programid);
		}
		if (hasProgramid) {
			existingProgram.setProgramid(programid);
		}
		if (programname != null) {
			existingProgram.setProgramname(programname);
		}
		if (programtype != null) {
			existingProgram.setProgramtype(programtype);
		}
		if (programdescription != null) {
			existingProgram.setProgramdescription(programdescription);
		}

		return existingProgram;
	}

	public Program toProgram() {
		Program newProgram = new Program();
		return toProgram(newProgram);
	}

	public long getProgramid() {
		return programid;
	}

	public void setProgramid(long programid) {
		this.hasProgramid = true;
		this.programid    = programid;
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

	public boolean hasProgramid() {
		return hasProgramid;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "ProgramIn{" + "programid=" + programid + ", programname='" + programname + '\'' + ", programtype='" +
		       programtype + '\'' + ", programdescription='" + programdescription + '\'' + ", hasProgramid=" +
		       hasProgramid + ", tags=" + tags + '}';
	}

}
