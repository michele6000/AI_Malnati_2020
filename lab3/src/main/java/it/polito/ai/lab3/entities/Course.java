package it.polito.ai.lab3.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Course {

    @Id
    private String name;

    private int min;
    private int max;
    private boolean enabled;

    @ManyToMany(mappedBy = "courses")
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
