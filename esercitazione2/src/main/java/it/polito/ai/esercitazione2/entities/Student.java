package it.polito.ai.esercitazione2.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Student {

    @Id
    private String id;
    private String name;
    private String firstName;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "student_course",joinColumns = @JoinColumn(name="student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_name"))
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course){
        courses.add(course);
        course.getStudents().add(this);
    }

}
