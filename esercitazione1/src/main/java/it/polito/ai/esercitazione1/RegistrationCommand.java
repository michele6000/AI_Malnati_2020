package it.polito.ai.esercitazione1;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {

    @NotBlank(message = "Campo obbligatorio!")
    @Size(max = 64, message = "{command.name.max.message}")
    @Size(min = 2, message = "{command.name.min.message}")
    private String name;

    @NotBlank(message = "Campo obbligatorio!")
    @Size(max = 64, message = "{command.surname.max.message}")
    @Size(min = 2, message = "{command.surname.min.message}")
    private String surname;

    @NotBlank(message = "Campo obbligatorio!")
    @Email(message = "{command.email.message}")
    private String email;

    @NotBlank()
    @Size(max = 64)
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Campo obbligatorio!")
    @Size(max = 64, message = "{command.password.max.message}")
    @Size(min = 8, message = "{command.password.min.message}")
    private String password2;

    private boolean privacy;

}
