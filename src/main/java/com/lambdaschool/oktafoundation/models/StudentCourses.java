package com.lambdaschool.oktafoundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "studentcourses")
@IdClass(StudentCoursesId.class)
public class StudentCourses extends Auditable implements Serializable
{

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "studentid")
    @JsonIgnoreProperties(value = "courses",
    allowSetters = true)
    private Student student;

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "courseid")
    @JsonIgnoreProperties(value = "students",
    allowSetters = true)
    private Course course;

    public StudentCourses()
    {
    }

    public StudentCourses(
         Student student,
         Course course)
    {
        this.student = student;
        this.course = course;
    }

    public Student getStudent()
    {
        return student;
    }

    public void setStudent(Student student)
    {
        this.student = student;
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
        if (!(o instanceof StudentCourses))
        {
            return false;
        }
        StudentCourses that = (StudentCourses) o;
        return ((student == null) ? 0 : student.getStudentid()) == ((that.student == null) ? 0 : that.student.getStudentid()) &&
            ((course == null) ? 0 : course.getCourseid()) == ((that.course == null) ? 0 : that.course.getCourseid());
    }

    @Override
    public int hashCode()
    {
        return 37;
    }
}
