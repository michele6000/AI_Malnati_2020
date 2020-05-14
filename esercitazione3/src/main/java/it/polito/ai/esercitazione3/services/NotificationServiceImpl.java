package it.polito.ai.esercitazione3.services;

import it.polito.ai.esercitazione3.dtos.TeamDTO;
import it.polito.ai.esercitazione3.entities.Token;
import it.polito.ai.esercitazione3.repositories.TeamRepository;
import it.polito.ai.esercitazione3.repositories.TokenRepositories;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    JavaMailSender emailSender;
    @Autowired
    TokenRepositories tokenRepo;
    @Autowired
    TeamService teamService;

    @Override
    public void sendMessage(String address, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

    @Override
    public boolean confirm(String token) {
        Date date = new Date();
        boolean expired = tokenRepo.findAllByExpiryBefore(new Timestamp(date.getTime())).stream().anyMatch(t -> t.getId().equals(token));
        if (expired || !tokenRepo.existsById(token))
            return false;
        Long id = tokenRepo.getOne(token).getTeamId();
        tokenRepo.deleteById(token);
        if(tokenRepo.findAllByTeamId(id).size() <= 0){
            return teamService.setActive(id);
        }
        return false;
    }

    @Override
    public boolean reject(String token) {
        Date date = new Date();
        boolean expired = tokenRepo.findAllByExpiryBefore(new Timestamp(date.getTime())).stream().anyMatch(t -> t.getId().equals(token));
        if (expired || !tokenRepo.existsById(token))
            return false;
        Long id = tokenRepo.getOne(token).getTeamId();
        tokenRepo.findAllByTeamId(id)
                .forEach(t -> {tokenRepo.delete(t);});
        return teamService.evictTeam(id);

    }

    @Override
    public void notifyTeam(TeamDTO team, List<String> membersIds) {
        Date date = new Date();
        Timestamp expire = new Timestamp(DateUtils.addHours(date,1).getTime());

        membersIds.forEach(id ->{
            String token = UUID.randomUUID().toString();
            Token memberToken = new Token();
            memberToken.setId(token);
            memberToken.setExpiryDate(expire);
            memberToken.setTeamId(team.getId());
            tokenRepo.save(memberToken);

            String email ="s"+id+"@studenti.polito.it";
            String body = "Accept by click here : http://localhost:8080/API/notification/accept/"+token +
                        "\nDecline by click here:http://localhost:8080/API/notification/reject/"+token;

            this.sendMessage("s56665@studenti.polito.it","Team Propose",body);
        });
    }

}
