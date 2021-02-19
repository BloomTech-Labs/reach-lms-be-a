package com.lambdaschool.oktafoundation.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserProgramsId implements Serializable
{
    private long user;
    private long program;

    public UserProgramsId()
    {
    }

    public UserProgramsId(long user, long program)
    {
        this.user = user;
        this.program = program;
    }

    public long getUser()
    {
        return user;
    }

    public void setUser(long user)
    {
        this.user = user;
    }

    public long getProgram()
    {
        return program;
    }

    public void setProgram(long program)
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
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        UserProgramsId that = (UserProgramsId) o;
        return user == that.user &&
                program == that.program;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getUser(), getProgram());
    }
}
