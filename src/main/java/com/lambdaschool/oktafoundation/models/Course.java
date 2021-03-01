package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programid")
    @JsonIgnoreProperties(value = "courses")
    private Program program;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "course",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties(value = "course",
        allowSetters = true)
    private Set<StudentCourses> students = new HashSet<>();

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnoreProperties(value = "course",
        allowSetters = true)
    private Set<TeacherCourses> teachers = new HashSet<>();
/*
    @OneToMany
        (mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
    @JsonIgnoreProperties(value = "course",
    allowSetters = true)
    private List<Teacher> teachers = new ArrayList<>();
*/
    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnoreProperties(value = "course",
        allowSetters = true)
    private Set<Module> Modules = new HashSet<>();

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

    public Set<StudentCourses> getStudents()
    {
        return students;
    }

    public void setStudents(Set<StudentCourses> students)
    {
        this.students = students;
    }

    public Set<Module> getModules()
    {
        return Modules;
    }

    public void setModules(Set<Module> modules)
    {
        Modules = modules;
    }

    public Set<TeacherCourses> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(Set<TeacherCourses> teachers)
    {
        this.teachers = teachers;
    }
}
