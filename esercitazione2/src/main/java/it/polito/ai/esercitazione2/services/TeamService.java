package it.polito.ai.esercitazione2.services;

import it.polito.ai.esercitazione2.dtos.CourseDTO;
import it.polito.ai.esercitazione2.dtos.StudentDTO;

import java.io.Reader;
import java.util.List;
import java.util.Optional;

public interface TeamService {

    boolean addCourse(CourseDTO course);
    Optional<CourseDTO> getCourse(String name);
    List<CourseDTO> getAllCourses();
    boolean addStudent(StudentDTO student);
    Optional<StudentDTO> getStudent(String studentId);
    List<StudentDTO> getAllStudents();
    List<StudentDTO> getEnrolledStudents(String courseName);
    boolean addStudentToCourse(String studentId, String courseName);
    void enableCourse(String courseName);
    void disableCourse(String courseName);
    List<Boolean>addAll(List<StudentDTO>students);
    List<Boolean>enrollAll(List<String> studentsIds,String courseName);
    List<CourseDTO>addAndEnroll(Reader r, String courseName);
}
