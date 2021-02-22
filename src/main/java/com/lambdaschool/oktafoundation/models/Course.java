package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.swing.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long courseid;

    @NotNull
    private String coursename;

    @NotNull
    private String coursecode;

    private String coursedescription;

    @ManyToOne
    @JoinColumn(name = "programid", nullable = false)
    @JsonIgnoreProperties(value = "courses")
    private Program program;

    public Course()
    {
    }

    public Course(
        @NotNull String coursename,
        @NotNull String coursecode,
        String coursedescription,
        Program program)
    {
        this.coursename = coursename;
        this.coursecode = coursecode;
        this.coursedescription = coursedescription;
        this.program = program;
    }

    public long getCourseid()
    {
        return courseid;
    }

    public void setCourseid(long courseid)
    {
        this.courseid = courseid;
    }

    public String getCoursename()
    {
        return coursename;
    }

    public void setCoursename(String coursename)
    {
        this.coursename = coursename;
    }

    public String getCoursecode()
    {
        return coursecode;
    }

    public void setCoursecode(String coursecode)
    {
        this.coursecode = coursecode;
    }

    public String getCoursedescription()
    {
        return coursedescription;
    }

    public void setCoursedescription(String coursedescription)
    {
        this.coursedescription = coursedescription;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram(Program program)
    {
        this.program = program;
    }
}
