package com.lambdaschool.oktafoundation.models;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "modules")
public class Module extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long moduleid;

    @Column(nullable = false)
    private String modulename;

    private String moduledescription;

    @Column(nullable = false)
    private String modulecontent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", nullable = false)
    @JsonIgnoreProperties(value = "modules")
    private Course course;

    public Module()
    {
    }

    public Module(String modulename, String moduledescription, String modulecontent, Course course)
    {
        this.modulename = modulename;
        this.moduledescription = moduledescription;
        this.modulecontent = modulecontent;
        this.course = course;
    }

    public long getModuleid()
    {
        return moduleid;
    }

    public void setModuleid(long moduleid)
    {
        this.moduleid = moduleid;
    }

    public String getModulename()
    {
        return modulename;
    }

    public void setModulename(String modulename)
    {
        this.modulename = modulename;
    }

    public String getModuledescription()
    {
        return moduledescription;
    }

    public void setModuledescription(String moduledescription)
    {
        this.moduledescription = moduledescription;
    }

    public String getModulecontent()
    {
        return modulecontent;
    }

    public void setModulecontent(String modulecontent)
    {
        this.modulecontent = modulecontent;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }
}
