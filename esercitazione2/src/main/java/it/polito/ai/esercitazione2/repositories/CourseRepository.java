package it.polito.ai.esercitazione2.repositories;

import it.polito.ai.esercitazione2.entities.Course;
import it.polito.ai.esercitazione2.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query("SELECT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.name=:courseName")
    List<Student> getStudentsInTeams(String courseName);

    @Query("SELECT s FROM Student s INNER JOIN s.courses c WHERE c.name=:courseName AND s NOT IN " +
            "(SELECT s FROM Student s INNER JOIN s.teams t INNER JOIN t.course c WHERE c.name=:courseName)")
    List<Student> getStudentsNotInTeams(String courseName);


}
