package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "teachercourses")
@IdClass(TeacherCoursesId.class)
public class TeacherCourses extends Auditable
    implements Serializable
{

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "teacherid")
    @JsonIgnoreProperties(value = "courses",
        allowSetters = true)
    private Teacher teacher;

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "courseid")
    @JsonIgnoreProperties(value = "teachers",
        allowSetters = true)
    private Course course;


    public TeacherCourses()
    {
    }

    public TeacherCourses(
        @NotNull Teacher teacher,
        @NotNull Course course)
    {
        this.teacher = teacher;
        this.course = course;
    }

    public Teacher getTeacher()
    {
        return teacher;
    }

    public void setTeacher(Teacher teacher)
    {
        this.teacher = teacher;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof TeacherCourses))
        {
            return false;
        }
        TeacherCourses that = (TeacherCourses) o;
        return ((teacher == null) ? 0 : teacher.getTeacherid()) == ((that.teacher == null) ? 0 : that.teacher.getTeacherid()) &&
            ((course == null) ? 0 : course.getCourseid()) == ((that.course == null) ? 0 : that.course.getCourseid());
    }

    @Override
    public int hashCode()
    {
        return 37;
    }
}
