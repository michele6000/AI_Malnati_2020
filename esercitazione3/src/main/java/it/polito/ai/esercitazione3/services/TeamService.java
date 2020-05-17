package it.polito.ai.esercitazione3.services;

import it.polito.ai.esercitazione3.dtos.CourseDTO;
import it.polito.ai.esercitazione3.dtos.ProfessorDTO;
import it.polito.ai.esercitazione3.dtos.StudentDTO;
import it.polito.ai.esercitazione3.dtos.TeamDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.Reader;
import java.util.List;
import java.util.Optional;

public interface TeamService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean addCourse(CourseDTO course);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Optional<CourseDTO> getCourse(String name);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<CourseDTO> getAllCourses();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean addStudent(StudentDTO student);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Optional<StudentDTO> getStudent(String studentId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<StudentDTO> getAllStudents();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<StudentDTO> getEnrolledStudents(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean addStudentToCourse(String studentId, String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void enableCourse(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void disableCourse(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Boolean> addAll(List<StudentDTO> students);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Boolean> enrollAll(List<String> studentsIds, String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Boolean> addAndEnroll(Reader r, String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<CourseDTO> getCourses(String studentId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<TeamDTO> getTeamsForStudent(String studentId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<StudentDTO> getMembers(Long teamId);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    TeamDTO proposeTeam(String courseId, String name, List<String> membersIds);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<TeamDTO> getTeamForCourse(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<StudentDTO> getStudentsInTeams(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<StudentDTO> getAvailableStudents(String courseName);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean setActive(Long id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean evictTeam(Long id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void userSaver(String username, String password, String role);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    boolean addProfesor(ProfessorDTO professor);

}
