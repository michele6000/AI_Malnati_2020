package it.polito.ai.esercitazione1;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginCommand {
    @NotBlank(message = "Devi inserire questo campo!")
    private String username;
    @NotBlank(message = "Devi inserire questo campo!")
    private String password;
}
