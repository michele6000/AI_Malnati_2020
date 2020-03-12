package it.polito.ai;

import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
//        String[] words = {"alfa", "beta", "gamma", "delta"};

        //Proviamo ad utilizzare gli stream

        //Conversione array to stream
//        Arrays.stream(words)
        Stream.of("alfa", "beta", "gamma", "delta") // Alternativa, e posso eliminare la def di words
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

// Slide 15 esempi:
        Stream.iterate(1, i -> i + 1) // a partire da 1 generami un flusso applicando l'op unario che dice i->i+1
                .limit(10) //se non fosse per limit che seleziona i primi n ellementi (10) del flusso andremmo avanti all'infinito
                .forEach(System.out::println); // stampo a video

        Stream.generate(() -> Math.random()) // mi genera dei numeri randomini compresi tra zero e uno
                .limit(10)
                .forEach(System.out::println);

        Stream<Double> s1 = Stream.generate(() -> Math.random()).limit(10); // salvo stream nella var s1
        Stream<Double> s2 = Stream.iterate(1.0, d -> d + 0.25).limit(10);

        //Concatenazione di stream
        Stream.concat(s1, s2)
                .forEach(System.out::println);
        //Ottengo 20 righe in uscita, 10 numeri random e 10 con i numeri generati in seuqenza x+0.25

// Slide 18 esempio reduce
        Stream<Double> s3 = Stream.iterate(1.0, d -> d + 0.25).limit(10);
        System.out.println(s3.reduce(0.0, (a, b) -> a + b));
        // Parto da un valore iniziale detto valore di accumulazione, ogni volta che mi entra un nuovo elemento nel
        // flusso io combinerò l'ementeo entrato con l'accumulatore precedente usando la funzione che ho passato
        // in questo caso l'operatore binario. Il flusso s2 comincia con 1, il primo elemento comporterà 1+0, che
        // diventerà il nuovo accumulatore, poi combinato con .25, e arrivo a 1.25 ecc..

        Stream<Double> s4 = Stream.iterate(1.0, d -> d + 0.25).limit(10);
        System.out.println(s4.reduce("", (a, b) -> a + " " + b, (a, b) -> a + " " + b));
        // concetto di BiOperation vedi slide
        // viene accumulato nelle stringa vuota tutti i numeri che avevo nello stream

    }
}
