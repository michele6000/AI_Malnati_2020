package it.polito.ai.esercitazione3.services;

import it.polito.ai.esercitazione3.dtos.TeamDTO;

import java.util.List;

public interface NotificationService {
    void sendMessage(String address, String Subject, String body);
    boolean confirm(String token);
    boolean reject(String token);
    void notifyTeam (TeamDTO team, List<String> membersIds);
}
