package it.polito.ai.es1.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
// Servono a potenziare la classe che abbiamo scritto aggiungendo tutto quello che avremmoo dovuto scrivere
// Per esempio i getter e setter, i toString, Hash adatta ad essere compatibile con i contenuti dei campi
// Anche un costruttore ad hoc verrà creato in base ai parametri indicati.


public class Message {
    private String test;
    private LocalDateTime time;

}
