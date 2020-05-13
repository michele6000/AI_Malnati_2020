package it.polito.ai.esercitazione3.controllers;

import it.polito.ai.esercitazione3.dtos.StudentDTO;
import it.polito.ai.esercitazione3.services.TeamService;
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

    @PostMapping({"", "/"})
    public StudentDTO addStudent(@RequestBody StudentDTO student) {
        if (service.addStudent(student))
            return ModelHelper.enrich(student);
        throw new ResponseStatusException(HttpStatus.CONFLICT, student.getId());

    }

    @GetMapping("/{id}")
    public StudentDTO getOne(@PathVariable String id) {
        Optional<StudentDTO> result = service.getStudent(id);
        if (result.isPresent())
            return ModelHelper.enrich(result.get());
        throw new ResponseStatusException(HttpStatus.CONFLICT, id);
    }
}
