package com.lambdaschool.oktafoundation.models;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TeacherCoursesId
    implements Serializable
{
    private long teacher;

    private long course;

    public TeacherCoursesId()
    {
    }

    public long getTeacher()
    {
        return teacher;
    }

    public void setTeacher(long teacher)
    {
        this.teacher = teacher;
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
        TeacherCoursesId that = (TeacherCoursesId) o;
        return teacher == that.teacher &&
            course == that.course;
    }

    @Override
    public int hashCode()
    {
        return 37;
    }
}
