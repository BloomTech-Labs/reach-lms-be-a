package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long teacherid;

    private String teachername;

    @OneToMany(fetch = FetchType.EAGER,
        mappedBy = "teacher",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnoreProperties(value = "teacher",
        allowSetters = true)
    private Set<TeacherCourses> courses = new HashSet<>();


    public Teacher()
    {
    }

    public Teacher(
        String teachername)
    {
        this.teachername = teachername;
    }

    public long getTeacherid()
    {
        return teacherid;
    }

    public void setTeacherid(long teacherid)
    {
        this.teacherid = teacherid;
    }

    public String getTeachername()
    {
        return teachername;
    }

    public void setTeachername(String teachername)
    {
        this.teachername = teachername;
    }

    public Set<TeacherCourses> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<TeacherCourses> courses)
    {
        this.courses = courses;
    }
}
