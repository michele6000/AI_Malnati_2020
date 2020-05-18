package it.polito.ai.lab3.controllers;

import it.polito.ai.lab3.dtos.CourseDTO;
import it.polito.ai.lab3.dtos.StudentDTO;
import it.polito.ai.lab3.dtos.TeamDTO;
import it.polito.ai.lab3.exceptions.StudentNotFoundException;
import it.polito.ai.lab3.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/API/students")
public class StudentController {

    @Autowired
    TeamService service;

    @GetMapping({"", "/"})
    public List<StudentDTO> all() {

        List<StudentDTO> students = service.getAllStudents();
        students.forEach(ModelHelper::enrich);
        return students;
    }

    @GetMapping("/{id}")
    public StudentDTO getOne(@PathVariable String id) {
        Optional<StudentDTO> student = service.getStudent(id);

        if (!student.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, id);

        else {
            StudentDTO studentDTO = student.get();
            return ModelHelper.enrich(studentDTO);
        }
    }

    @PostMapping({"", "/"})
    public StudentDTO addStudent(@RequestBody StudentDTO dto) {

        if (!service.addStudent(dto))
            throw new ResponseStatusException(HttpStatus.CONFLICT, dto.getId());
        else return ModelHelper.enrich(dto);
    }

    @GetMapping("/{id}/courses")
    public List<CourseDTO> getCourses(@PathVariable String id) {
        try {
            List<CourseDTO> courses = service.getCourses(id);
            return courses;
        } catch (StudentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/teams")
    public List<TeamDTO> getTeamsForStudent(@PathVariable String id) {
        try {
            List<TeamDTO> teams = service.getTeamsForStudent(id);
            return teams;
        } catch (StudentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
