package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long studentid;

    private String studentname;

    @OneToMany( fetch = FetchType.EAGER,
        mappedBy = "student",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties(value = "student",
        allowSetters = true)
    private Set<StudentCourses> courses = new HashSet<>();

    public Student()
    {
    }

    public Student(String studentname)
    {
        this.studentname = studentname;
    }

    public long getStudentid()
    {
        return studentid;
    }

    public void setStudentid(long studentid)
    {
        this.studentid = studentid;
    }

    public String getStudentname()
    {
        return studentname;
    }

    public void setStudentname(String studentname)
    {
        this.studentname = studentname;
    }

    public Set<StudentCourses> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<StudentCourses> courses)
    {
        this.courses = courses;
    }
}
