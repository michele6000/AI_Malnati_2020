package it.polito.ai.esercitazione2.repositories;

import it.polito.ai.esercitazione2.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
}
