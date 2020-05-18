package it.polito.ai.esercitazione3.controllers;

import it.polito.ai.esercitazione3.dtos.ProfessorDTO;
import it.polito.ai.esercitazione3.exceptions.TeamServiceException;
import it.polito.ai.esercitazione3.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    TeamService service;

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails){
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> ((GrantedAuthority) a).getAuthority())
                .collect(Collectors.toList())
        );
        return ResponseEntity.ok(model);
    }
    @PostMapping("/addProfessor")
    public ProfessorDTO addProfessor(@RequestBody ProfessorDTO professor){
        if(service.addProfessor(professor))
            return professor;
        return new ProfessorDTO();
    }

    @PostMapping("/addProfessorToCourse")
    public boolean addProfessorToCourse(@RequestParam String profId, @RequestParam String courseName){
        try{
        return service.addProfessorToCourse(profId,courseName);}
        catch (TeamServiceException e){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
