package it.polito.ai.esercitazione3.controllers;

import it.polito.ai.esercitazione3.dtos.CourseDTO;
import it.polito.ai.esercitazione3.dtos.StudentDTO;
import it.polito.ai.esercitazione3.services.CourseNotFoundException;
import it.polito.ai.esercitazione3.services.StudentNotFoundException;
import it.polito.ai.esercitazione3.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/API/courses")
public class CourseController {

    @Autowired
    TeamService service;


    @GetMapping({"", "/"})
    public List<CourseDTO> all() {
        List<CourseDTO> courses = service.getAllCourses();
        courses.forEach(ModelHelper::enrich);
        return courses;
    }


    @PostMapping({"", "/"})
    public CourseDTO addCourse(@RequestBody CourseDTO course) {
        if (service.addCourse(course))
            return ModelHelper.enrich(course);
        throw new ResponseStatusException(HttpStatus.CONFLICT, course.getName());

    }

    @GetMapping("/{name}")
    public CourseDTO getOne(@PathVariable String name) {
        Optional<CourseDTO> result = service.getCourse(name);
        if (result.isPresent())
            return ModelHelper.enrich(result.get());
        throw new ResponseStatusException(HttpStatus.CONFLICT, name);

    }

    @GetMapping("/{name}/enrolled")
    public List<StudentDTO> enrolledStudents(@PathVariable String name) {
        if (!service.getCourse(name).isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, name);
        List<StudentDTO> students = service.getEnrolledStudents(name);
        students.forEach(ModelHelper::enrich);
        return students;
    }

    @PostMapping("/{name}/enrollOne")
    public boolean enrollOne(@PathVariable String name, @RequestBody StudentDTO stud) {
        String id = stud.getId();
        try {
            if (service.addStudentToCourse(id, name))
                return true;
            throw new ResponseStatusException(HttpStatus.CONFLICT, id);
        } catch (CourseNotFoundException | StudentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrolling: " + id + " - " + name + ". Error : " + e.getMessage());
        }
    }

    @PostMapping("/{name}/enrollMany")
    public List<Boolean> enrollStudents(@PathVariable String name, @RequestParam("file") MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "text/csv"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Filename: " + file.getName() + " Type: " + file.getContentType());
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            return service.addAndEnroll(reader, name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrolling: " + file.getName() + ".csv - " + name + ". Error : " + e.getMessage());
        }
    }
}















