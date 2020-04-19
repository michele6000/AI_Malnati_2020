package it.polito.ai.esercitazione2.dtos;

import com.opencsv.bean.CsvBindByName;
import it.polito.ai.esercitazione2.entities.Student;
import lombok.Data;

@Data
public class StudentDTO {
    @CsvBindByName
    String id;
    @CsvBindByName
    String name;
    @CsvBindByName
    String firstName;
}
