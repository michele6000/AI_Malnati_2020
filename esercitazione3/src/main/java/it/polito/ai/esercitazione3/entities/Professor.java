package it.polito.ai.esercitazione3.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Professor {
    @Id
    String id;
    String firstName;
    String name;
    @JoinTable(name="professor_course",
            joinColumns = @JoinColumn(name="professor_id"),
            inverseJoinColumns = @JoinColumn(name="course_name")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Course> courses = new ArrayList<>();
    Long userId;

    public boolean addCourses(Course c){
        if(courses.contains(c)){
            return false;
        }
        if(courses.add(c)){
            c.professors.add(this);
            return true;
        } else {
            return false;
        }
    }
}
