package it.polito.ai.esercitazione2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    private String name;
    private int min;
    private int max;
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student){
        students.add(student);
        student.getCourses().add(this);
    }
}
