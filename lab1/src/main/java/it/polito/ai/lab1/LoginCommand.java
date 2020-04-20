package it.polito.ai.lab1;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class LoginCommand {
    @Email(message = "{command.mail.message}")
    private String mail;
    private String password;
}
