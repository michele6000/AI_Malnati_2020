package it.polito.ai.esercitazione3.services;

public class TeamNotFoundException extends TeamServiceException {
    public TeamNotFoundException(String s) {
        super(s);
    }
}
