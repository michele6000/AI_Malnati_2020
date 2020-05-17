package it.polito.ai.lab3.services;

import it.polito.ai.lab3.dtos.TeamDTO;
import it.polito.ai.lab3.entities.Token;
import it.polito.ai.lab3.exceptions.TeamServiceException;
import it.polito.ai.lab3.repositories.TokenRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    final static String URL_BASE="http://localhost:8080/API";

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private TeamService teamService;

    @Override
    public void sendMessage(String address, String subject, String body) {

        address="paola.caso96@gmail.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);

    }

    @Override
    public boolean confirm(String token) {
        //token not existent
        if(!tokenRepo.existsById(token))
            return false;

        Long teamId=tokenRepo.getOne(token).getTeamId();

        //token expired
        if(tokenRepo.findAllByExpiryDateBefore(new Timestamp(new Date().getTime())).stream()
                .anyMatch(t -> t.getId().equals(token))) {
            teamService.evictTeam(teamId);
            return false;
        }

        tokenRepo.deleteById(token);

        if(tokenRepo.findAllByTeamId(teamId).size()==0){
            try {
                teamService.setActive(teamId);
                return true;
            }
            catch(TeamServiceException e){
                throw e;
            }
        }
        else return false;
    }

    @Override
    public boolean reject(String token) {
        //token not existent
        if(!tokenRepo.existsById(token))
            return false;
        //token expired
        if(tokenRepo.findAllByExpiryDateBefore(new Timestamp(new Date().getTime())).stream().anyMatch(t -> t.getId().equals(token)))
            return false;

        Long teamId=tokenRepo.getOne(token).getTeamId();
        tokenRepo.findAllByTeamId(teamId).forEach(t->tokenRepo.delete(t));
        try {
            teamService.evictTeam(teamId);
            return true;
        }
        catch(TeamServiceException e){
            throw e;
        }
    }

    @Override
    public void notifyTeam(TeamDTO dto, List<String> memberIds) {

        Long teamId=dto.getId();
        Timestamp expiryDate;
        for (String m : memberIds) {
            String id=UUID.randomUUID().toString();
            expiryDate=new Timestamp(DateUtils.addHours(new Date(),1).getTime());
            Token token=new Token();
            token.setId(id);
            token.setTeamId(teamId);
            token.setExpiryDate(expiryDate);
            tokenRepo.save(token);
            String address="s"+m+"@studenti.polito.it";
            String body="CONFIRM participation to the team: "+URL_BASE+"/notifications/confirm/"+id+
                    "\nREJECT participation to the team: "+URL_BASE+"/notifications/reject/"+id;
            this.sendMessage(address,"Team proposal",body);
        }

    }
}
