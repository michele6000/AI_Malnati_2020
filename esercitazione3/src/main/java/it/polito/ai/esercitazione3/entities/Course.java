package it.polito.ai.esercitazione3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    List<Professor> professors = new ArrayList<>();

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

    public boolean addProfessor(Professor p){
        if(professors.contains(p)){
            return false;
        }
        if(professors.add(p)){
            p.courses.add(this);
            return true;
        } else {
            return false;
        }
    }


}
