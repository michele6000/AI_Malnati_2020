package it.polito.ai.lab1;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class RegistrationDetails {
    String name;
    String surname;
    String mail;
    String password;
    Date registrationDate;
}
