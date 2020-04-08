package it.polito.ai.esercitazione1;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class RegistrationDetails {

    String name;
    String surname;
    String email;
    String password;
    Date registrationDate;

}
