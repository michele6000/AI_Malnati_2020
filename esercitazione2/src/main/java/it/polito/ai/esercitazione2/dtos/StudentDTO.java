package it.polito.ai.esercitazione2.dtos;

import com.opencsv.bean.CsvBindByName;
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
