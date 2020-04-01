package it.polito.ai.boot1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// L'annotazione permette al codice di fare un sacco di cose, è compresa @configuration
@SpringBootApplication
public class Boot1Application {

    public static void main(String[] args) {
        SpringApplication.run(Boot1Application.class, args);
    }

    @Bean
    public Date last() {
        return new Date();
    }
  
    @Bean
    public List<String> items(Date last) {
        return Stream.of("alfa", "beta", "gamma", last.toString()).collect(Collectors.toList());
    }

}
