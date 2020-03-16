package it.polito.ai.mavenExample;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class main {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(); //oggetti java <---> JSON e viceversa <3
        Message msg1 = new Message("Hello, Maven!", LocalDateTime.now());
        System.out.println(objectMapper.writeValueAsString(msg1));

    }
}

// se lancio nativo NoClassDefFoundError:
// Ho trovato la classe object map quando ho compilato, m quando ho eseguito no, e quindi
// non sono più in grado di fare l'esecuzione. Java ha precaricate soltanto un set di classi minime, non tutte
// la classe da noi desiderata non c'e'.
// MAven ha scaricato il pezzo che mancava, l'ha reso disponibilie durante la compilazione, in m2, e poi
// se ne è fregato. Se vogliamo che sia disponibile anche in runtime dobbiamo fare un po di cose in più...

// Maven scarica le dipendenze che mancano al posto nostro

// Tipo *Error non gestibile, causa il crash e basta.

