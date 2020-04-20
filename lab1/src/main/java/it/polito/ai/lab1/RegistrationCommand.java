package it.polito.ai.lab1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {

    @NotBlank(message = "{command.name.blank.message}")
    @Size(min = 2, message = "{command.name.min.message}")
    @Size(max = 100, message = "{command.name.max.message}")
    private String name;

    @NotBlank(message = "{command.surname.blank.message}")
    @Size(min = 2, message = "{command.surname.min.message}")
    @Size(max = 100, message = "{command.surname.max.message}")
    private String surname;

    @NotBlank(message = "{command.mail.blank.message}")
    @Email(message = "{command.mail.message}")
    private String mail;

    @NotBlank(message = "{command.password.blank.message}")
    @Size(min = 8, message = "{command.password.min.message}")
    @Size(max = 100, message = "{command.password.max.message}")
    private String password;

    @NotBlank(message = "{command.password2.blank.message}")
    @Size(min = 8, message = "{command.password2.min.message}")
    @Size(max = 100, message = "{command.password2.max.message}")
    private String password2;

    private boolean privacy_accepted;


}
