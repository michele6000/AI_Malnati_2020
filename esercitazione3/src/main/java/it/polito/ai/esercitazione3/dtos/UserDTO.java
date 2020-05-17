package it.polito.ai.esercitazione3.dtos;

import lombok.Data;

@Data
public class UserDTO {
    Long id;
    String username;
    String password;
    String role;
}
