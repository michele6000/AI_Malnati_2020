package it.polito.ai.esercitazione3.dtos;

import it.polito.ai.esercitazione3.entities.Course;
import lombok.Data;

@Data
public class TeamDTO {
    private Long id;
    private String name;
    private int status;
    private Course course;
}
