package it.polito.ai;

import java.util.stream.IntStream;

public class IntStreamReduce {
    public int reduce(IntStream s) {
        return s.reduce((a, b) -> a + b).getAsInt();
        // Lancia un itnero se presente altrimenti un eccezione.
    }
}
