package it.polito.ai.esercitazione2.services;

public class StudentNotFoundException  extends TeamServiceException{

    public StudentNotFoundException(String s) {
        super(s);
    }
}
