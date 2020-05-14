package it.polito.ai.esercitazione3.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Token {
    @Id
    private String id;
    private Long teamId;
    private Timestamp expiryDate;
}
