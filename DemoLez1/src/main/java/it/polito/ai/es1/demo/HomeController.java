package it.polito.ai.es1.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
//Applicazione web in gradi di rispondere al protoccolo rest
// Questa classe gestirà le risposte http al metodo rest
public class HomeController {

    @GetMapping("/")
    // In nostro messaggio sarà accessibile mediante la hall / del localhost:8080, fatto in auto
    // dal sistema di annotazioni
    public Message home() {
        return new Message("Benvenuti al corso!", LocalDateTime.now());
    }

}
