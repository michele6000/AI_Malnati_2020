package it.polito.ai.lab3.controllers;

import it.polito.ai.lab3.dtos.CourseDTO;
import it.polito.ai.lab3.dtos.StudentDTO;
import it.polito.ai.lab3.services.CourseNotFoundException;
import it.polito.ai.lab3.services.StudentNotFoundException;
import it.polito.ai.lab3.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    @GetMapping("/{name}")
    public CourseDTO getOne(@PathVariable String name) {
        Optional<CourseDTO> course = service.getCourse(name);

        if (!course.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, name);

        else {
            CourseDTO courseDTO = course.get();
            return ModelHelper.enrich(courseDTO);
        }
    }

    @GetMapping("/{name}/enrolled")
    public List<StudentDTO> enrolledStudents(@PathVariable String name) {
        try {
            List<StudentDTO> students = service.getEnrolledStudents(name);
            students.forEach(ModelHelper::enrich);
            return students;
        } catch (CourseNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course: " + name + " Error: " + e.getMessage());
        }
    }

    @PostMapping({"", "/"})
    public CourseDTO addCourse(@RequestBody CourseDTO dto) {

        if (!service.addCourse(dto))
            throw new ResponseStatusException(HttpStatus.CONFLICT, dto.getName());
        else return ModelHelper.enrich(dto);
    }

    @PostMapping("/{name}/enrollOne")
    public StudentDTO enrollOne(@PathVariable String name, @RequestBody StudentDTO student) {

        try {
            if (service.addStudentToCourse(student.getId(), name))
                return student;
            else throw new ResponseStatusException(HttpStatus.CONFLICT, name + " " + student.getId());
        } catch (CourseNotFoundException | StudentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course: " + name + " StudentID: " + student.getId() + " Error: " + e.getMessage());
        }
    }

    @PostMapping("/{name}/enrollMany")
    public List<Boolean> enrollStudents(@PathVariable String name, @RequestParam("file") MultipartFile file) {

        if (!Objects.equals(file.getContentType(), "text/csv"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "File content type: " + file.getContentType() + " Error: CSV file required!");

        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            return service.addAndEnroll(reader, name);
        } catch (CourseNotFoundException | StudentNotFoundException | IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course: " + name + " Error: " + e.getMessage());
        }
    }


}
