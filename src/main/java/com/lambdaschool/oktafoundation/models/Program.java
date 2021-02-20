package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String programname;

    @Column(nullable = false)
    private String programtype;

    private String programdescription;

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
}

