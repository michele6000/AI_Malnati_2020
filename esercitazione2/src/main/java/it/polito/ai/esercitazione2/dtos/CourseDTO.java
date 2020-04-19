package it.polito.ai.esercitazione2.dtos;

import lombok.Builder;
import lombok.Data;

@Data

public class CourseDTO {

    String name;
    int min;
    int max;
    boolean enable;

}
