package it.polito.ai.lab3.dtos;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TokenDTO {
    String id;
    Long teamId;
    Timestamp expiryDate;
}
