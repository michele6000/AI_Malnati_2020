package it.polito.ai.test;

import it.polito.ai.IntStreamReduce;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntStreamReduceTest {

    private IntStreamReduce isr;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        isr = new IntStreamReduce();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        isr = null;
    }

    @Test
    void testSimpleStream() {
        assertEquals(6, isr.reduce(IntStream.of(1, 2, 3)));
    }

    @Test
    void testEmptyStream() {
        assertThrows(Exception.class, () -> isr.reduce(IntStream.empty()));
    }
}
