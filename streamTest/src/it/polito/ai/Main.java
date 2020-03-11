package it.polito.ai;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String[] words = {"alfa", "beta", "gamma", "delta"};

        //Proviamo ad utilizzare gli stream

        //Conversione array to stream
        Arrays.stream(words)
//                .filter(s-> s.endsWith("Ta")) //seleziono in base a terminazione
                .filter(s -> s.length() > 4) //Accetta come parametro un predicato, modo più semlice funzione lambda
                //nel nostro caso sto selezionando tutte le stringhe la cui lunghezza è > 4
                .parallel()
                .map(s -> {
                    System.out.println(Thread.currentThread());
                    return s;
                })
                .map(String::toUpperCase) //sto rendendo maiuscole tutte le stringhe risultanti dall'operazione filtro
//                .forEach(s-> System.out.println(s));
                .forEach(System.out::println); //stampo tutto

    }
}
