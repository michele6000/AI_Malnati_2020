package it.polito.ai.esercitazione2.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.esercitazione2.dtos.CourseDTO;
import it.polito.ai.esercitazione2.dtos.StudentDTO;
import it.polito.ai.esercitazione2.entities.Course;
import it.polito.ai.esercitazione2.entities.Student;
import it.polito.ai.esercitazione2.repositories.CourseRepository;
import it.polito.ai.esercitazione2.repositories.StudentRepository;
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
public class TeamServiceImpl implements TeamService{

    @Autowired
     ModelMapper modelMapper;

    @Autowired
     CourseRepository courseRepo;

    @Autowired
     StudentRepository studentRepo;

    @Override
    public boolean addCourse(CourseDTO course){
        if(course==null) return false;
        Course _course = modelMapper.map(course,Course.class);
        if (courseRepo.existsById(_course.getName())) return false;
        courseRepo.save(_course);
        return true;
    }

    @Override
    public boolean addStudent(StudentDTO student){
        if(student==null) return false;
        Student _student = modelMapper.map(student,Student.class);
        if (studentRepo.existsById(_student.getId())) return false;
        studentRepo.save(_student);
        return true;
    }

    @Override
    public boolean addStudentToCourse(String studentId, String courseName){
        if (studentId == null || courseName == null) return false;
        if (!studentRepo.findById(studentId).isPresent())
            throw new StudentNotFoundException("Studente non esistente!");
        if(!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Corso non esistente!");
        if(!courseRepo.getOne(courseName).isEnable())
            return false;
        Course _course = courseRepo.getOne(courseName);

        if(!_course.isEnable())
            return false;

        Optional<Student>found=_course.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst();
        if(found.isPresent())
            return false;

        Student _student = studentRepo.getOne(studentId);
       _course.addStudent(_student);
       return true;
    }

    @Override
    public Optional<CourseDTO> getCourse(String name){
        if (!courseRepo.existsById(name)) return Optional.empty();
        return Optional.of(modelMapper.map(courseRepo.getOne(name),CourseDTO.class));
    }

    @Override
    public Optional<StudentDTO> getStudent(String studentId){
        if(!studentRepo.existsById(studentId)) return Optional.empty();
        return  Optional.of(modelMapper.map(studentRepo.getOne(studentId),StudentDTO.class));
    }

    @Override
    public List<CourseDTO> getAllCourses(){
        return courseRepo.findAll()
                .stream()
                .map(l -> modelMapper.map(l,CourseDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<StudentDTO> getAllStudents(){
        return studentRepo.findAll()
                .stream()
                .map(l -> modelMapper.map(l,StudentDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<StudentDTO> getEnrolledStudents(String courseName){
        if(!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Corso non esistente!");
        return courseRepo.getOne(courseName)
                .getStudents()
                .stream()
                .map(l -> modelMapper.map(l,StudentDTO.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public void enableCourse(String courseName){
        if(!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Corso non esistente!");
        if(courseRepo.getOne(courseName).isEnable())
            return;
        courseRepo.getOne(courseName).setEnable(true);

    }

    @Override
    public void disableCourse(String courseName){
        if(!courseRepo.existsById(courseName))
            throw new CourseNotFoundException("Corso non esistente!");
        if(!courseRepo.getOne(courseName).isEnable())
            return;
        courseRepo.getOne(courseName).setEnable(false);
    }

    @Override
    public List<Boolean> addAll(List<StudentDTO> students) {
        List<Boolean> tmp = new ArrayList<>();
        students.forEach(s -> {
                    tmp.add(this.addStudent(s));
                });
        return tmp;
    }

    @Override
    public List<Boolean> enrollAll(List<String> studentsIds, String courseName){
        List<Boolean> tmp = new ArrayList<>();
        studentsIds.forEach(s -> {
            try{
            tmp.add(this.addStudentToCourse(s,courseName));}
            catch (StudentNotFoundException | CourseNotFoundException e){
//                System.out.println(e.getMessage() + " : " +s);
                throw e;
            }
        });
        return tmp;
    }

    @Override
    public List<CourseDTO> addAndEnroll(Reader r, String courseName) {
        CsvToBean<StudentDTO> csvToBean = new CsvToBeanBuilder<StudentDTO>(r)
                .withType(StudentDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<StudentDTO> students = csvToBean.parse();

        return null;
    }


}
