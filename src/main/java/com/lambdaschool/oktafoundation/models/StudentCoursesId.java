package com.lambdaschool.oktafoundation.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StudentCoursesId
    implements Serializable
{
    private long student;

    private long course;

    public StudentCoursesId()
    {
    }

    public long getStudent()
    {
        return student;
    }

    public void setStudent(long student)
    {
        this.student = student;
    }

    public long getCourse()
    {
        return course;
    }

    public void setCourse(long course)
    {
        this.course = course;
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass() )
        {
            return false;
        }
        StudentCoursesId that = (StudentCoursesId) o;
        return student == that.student &&
            course == that.course;
    }

    @Override
    public int hashCode()
    {
        return 37;
    }
}
