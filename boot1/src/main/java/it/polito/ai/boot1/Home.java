package it.polito.ai.boot1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Home {

    //  collego l'implementazione alla variabile
    private final TimestampService timestampService;
    private List<String> data;

    //    viene passatto come oggetto da mettere nel costruttore
    @Autowired
    public Home(TimestampService timestampService, List<String> data) {
        this.timestampService = timestampService;
        this.data = data;
    }

    @GetMapping("/")
    public String home() {
        return "Home page generated at: " + timestampService.getTimestamp() + " " +
                data.stream().collect(Collectors.joining(" - "));
    }
}
