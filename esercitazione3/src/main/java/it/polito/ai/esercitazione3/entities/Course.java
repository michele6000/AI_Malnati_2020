package it.polito.ai.esercitazione3.entities;

import lombok.Data;

import javax.persistence.*;
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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Team> teams;

    public void addStudent(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void addTeam(Team team) {
        teams.add(team);
        team.setCourse(this);
    }


}
