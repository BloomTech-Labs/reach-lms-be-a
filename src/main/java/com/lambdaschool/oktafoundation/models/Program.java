package com.lambdaschool.oktafoundation.models;

import javax.persistence.*;

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
}
