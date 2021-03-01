package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "programs")
public class Program extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long programid;


    private String programname;


    private String programtype;

    private String programdescription;

    @OneToMany(fetch = FetchType.LAZY,
    mappedBy = "program",
    cascade = CascadeType.REMOVE,
    orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties(value = "program",
        allowSetters = true)
    List<Course> courses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "programs")
    private User user;


    public Program()
    {
    }

    public Program(String programname, String programtype, String programdescription)
    {
        this.programname = programname;
        this.programtype = programtype;
        this.programdescription = programdescription;
    }

    public long getProgramid()
    {
        return programid;
    }

    public void setProgramid(long programid)
    {
        this.programid = programid;
    }

    public String getProgramname()
    {
        return programname;
    }

    public void setProgramname(String programname)
    {
        this.programname = programname;
    }

    public String getProgramtype()
    {
        return programtype;
    }

    public void setProgramtype(String programtype)
    {
        this.programtype = programtype;
    }

    public String getProgramdescription()
    {
        return programdescription;
    }

    public void setProgramdescription(String programdescription)
    {
        this.programdescription = programdescription;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public List<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(List<Course> courses)
    {
        this.courses = courses;
    }
}

