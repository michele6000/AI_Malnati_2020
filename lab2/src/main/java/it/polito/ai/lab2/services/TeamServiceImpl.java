package it.polito.ai.lab2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.lab2.dtos.CourseDTO;
import it.polito.ai.lab2.dtos.StudentDTO;
import it.polito.ai.lab2.dtos.TeamDTO;
import it.polito.ai.lab2.entities.Course;
import it.polito.ai.lab2.entities.Student;
import it.polito.ai.lab2.entities.Team;
import it.polito.ai.lab2.repositories.CourseRepository;
import it.polito.ai.lab2.repositories.StudentRepository;
import it.polito.ai.lab2.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Override
    public boolean addCourse(CourseDTO course) {
        if (course == null || course.getName().length() == 0)
            return false;
        Course courseEntity = modelMapper.map(course, Course.class);
        if (courseRepo.existsById(courseEntity.getName()))
            return false;
        courseRepo.save(courseEntity);
        return true;
    }

    @Override
    public boolean addStudent(StudentDTO student) {
        if (student == null || student.getId().length() == 0 || student.getName().length() == 0 || student.getFirstName().length() == 0)
            return false;
        Student studentEntity = modelMapper.map(student, Student.class);
        if (studentRepo.existsById(studentEntity.getId()))
            return false;
        studentRepo.save(studentEntity);
        return true;

    }

    @Override
    public boolean addStudentToCourse(String studentId, String courseName) {

        if (studentId.length() == 0 || courseName.length() == 0)
            return false;
        if (!studentRepo.existsById(studentId))
            throw new StudentNotFoundException("Student not found!");
        if (!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Course not found!");

        Course course = courseRepo.getOne(courseName);
        Student student = studentRepo.getOne(studentId);

        if (!course.isEnabled())
            return false;

        Optional<Student> found = course.getStudents()
                .stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst();
        if (found.isPresent())
            return false;

        course.addStudent(student);

        return true;

    }

    @Override
    public Optional<CourseDTO> getCourse(String name) {
        if (!courseRepo.existsById(name))
            return Optional.empty();

        return Optional.of(modelMapper.map(courseRepo.getOne(name), CourseDTO.class));

    }

    @Override
    public List<CourseDTO> getAllCourses() {

        return courseRepo.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CourseDTO.class))
                .collect(Collectors.toList())
                ;

    }

    @Override
    public Optional<StudentDTO> getStudent(String studentId) {

        if (!studentRepo.existsById(studentId))
            return Optional.empty();

        return Optional.of(modelMapper.map(studentRepo.getOne(studentId), StudentDTO.class));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepo.findAll()
                .stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList())
                ;

    }

    @Override
    public List<StudentDTO> getEnrolledStudents(String courseName) {
        if (!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Course not found!");

        return courseRepo.getOne(courseName)
                .getStudents()
                .stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public void enableCourse(String courseName) {
        if (courseRepo.existsById(courseName))
            courseRepo.getOne(courseName).setEnabled(true);
        else throw new CourseNotFoundException("Course not found!");

    }

    @Override
    public void disableCourse(String courseName) {
        if (courseRepo.existsById(courseName))
            courseRepo.getOne(courseName).setEnabled(false);
        else throw new CourseNotFoundException("Course not found!");

    }

    @Override
    public List<Boolean> addAll(List<StudentDTO> students) {
        List<Boolean> studentsAdded = new ArrayList<>();
        students.forEach(s -> studentsAdded.add(addStudent(s)));
        return studentsAdded;
    }

    @Override
    public List<Boolean> enrollAll(List<String> studentIds, String courseName) {
        List<Boolean> studentsEnrolled = new ArrayList<>();
        studentIds.forEach(s -> {
            try {
                studentsEnrolled.add(addStudentToCourse(s, courseName));
            } catch (StudentNotFoundException | CourseNotFoundException e) {
                throw e;
            }
        });
        return studentsEnrolled;
    }

    @Override
    public List<Boolean> addAndEnroll(Reader r, String courseName) {
        CsvToBean<StudentDTO> csvToBean = new CsvToBeanBuilder<StudentDTO>(r)
                .withType(StudentDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<StudentDTO> students = csvToBean.parse();

        List<Boolean> added = addAll(students);

        List<String> studentIds = students.stream()
                .map(s -> s.getId())
                .collect(Collectors.toList());
        try {
            List<Boolean> enrolled = enrollAll(studentIds, courseName);
            List<Boolean> addedAndEnrolled = new ArrayList<>();

            for (int i = 0; i < added.size(); i++)
                addedAndEnrolled.add(added.get(i) || enrolled.get(i));


            return addedAndEnrolled;
        } catch (StudentNotFoundException | CourseNotFoundException e) {
            throw e;
        }
    }

    @Override
    public List<CourseDTO> getCourses(String studentId) {
        if (!studentRepo.existsById(studentId))
            throw new StudentNotFoundException("Student not found!");

        return studentRepo.getOne(studentId).getCourses()
                .stream()
                .map(c -> modelMapper.map(c, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDTO> getTeamsForStudent(String studentId) {
        if (!studentRepo.existsById(studentId))
            throw new StudentNotFoundException("Student not found!");

        return studentRepo.getOne(studentId)
                .getTeams()
                .stream()
                .map(t -> modelMapper.map(t, TeamDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<StudentDTO> getMembers(Long teamId) {
        if (!teamRepo.existsById(teamId))
            throw new TeamServiceException("Team not found!");

        return teamRepo.getOne(teamId)
                .getMembers()
                .stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public TeamDTO proposeTeam(String courseId, String name, List<String> membersIds) {
        if (!courseRepo.existsById(courseId))
            throw new CourseNotFoundException("Course not found!");
        if (!courseRepo.getOne(courseId).isEnabled())
            throw new TeamServiceException("Course not enabled!");
        if (membersIds.size() > courseRepo.getOne(courseId).getMax())
            throw new TeamServiceException("Too many members for this team!");
        if (membersIds.size() < courseRepo.getOne(courseId).getMin())
            throw new TeamServiceException("Too few members for this team!");

        if (membersIds.size() > membersIds.stream().distinct().count())
            throw new TeamServiceException("Duplicated members in team proposal!");

        for (String m : membersIds) {
            if (!studentRepo.existsById(m))
                throw new StudentNotFoundException("Student not found!");
            if (!courseRepo.getOne(courseId).getStudents().contains(studentRepo.getOne(m)))
                throw new TeamServiceException("Student not enrolled in this course!");
            courseRepo.getOne(courseId).getTeams().forEach(t -> {
                if (t.getMembers().contains(studentRepo.getOne(m)))
                    throw new TeamServiceException("Student " + m + " is already member of a team (" + t.getName() + ") for this course!");
            });
        }

        Team team = new Team();
        team.setName(name);
        membersIds.forEach(m -> {
            team.addMember(studentRepo.getOne(m));
        });
        team.setCourse(courseRepo.getOne(courseId));
        team.setId((teamRepo.save(team).getId()));

        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public List<TeamDTO> getTeamForCourse(String courseName) {
        if (!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Course not found!");

        return courseRepo.getOne(courseName)
                .getTeams()
                .stream()
                .map(t -> modelMapper.map(t, TeamDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<StudentDTO> getStudentsInTeams(String courseName) {

        if (!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Course not found!");

        return courseRepo.getStudentsInTeams(courseName)
                .stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getAvailableStudents(String courseName) {
        if (!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Course not found!");

        return courseRepo.getStudentsNotInTeams(courseName)
                .stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());
    }


}
