package it.polito.ai.esercitazione2.services;

public class TeamNotFoundException extends TeamServiceException {
    public TeamNotFoundException(String s) {
        super(s);
    }
}
