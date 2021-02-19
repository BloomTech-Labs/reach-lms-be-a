package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "userprograms")
@IdClass(UserProgramsId.class)
public class UserPrograms extends Auditable implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "programs",
            allowSetters = true)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "programid")
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Program program;

    public UserPrograms()
    {
    }

    public UserPrograms(User user, Program program)
    {
        this.user = user;
        this.program = program;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram(Program program)
    {
        this.program = program;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof UserPrograms))
        {
            return false;
        }
        UserPrograms that = (UserPrograms) o;
        return ((user == null) ? 0 : user.getUserid()) == ((that.user == null) ? 0 : that.user.getUserid()) &&
                ((program == null) ? 0 : program.getProgramid()) == ((that.program == null) ? 0 : that.program.getProgramid());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getUser(), getProgram());
    }
}
