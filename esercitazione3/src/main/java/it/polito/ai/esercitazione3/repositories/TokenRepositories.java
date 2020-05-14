package it.polito.ai.esercitazione3.repositories;

import it.polito.ai.esercitazione3.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TokenRepositories  extends JpaRepository<Token,String> {
    List<Token>findAllByExpiryBefore(Timestamp t);
    List<Token>findAllByTeamId(Long teamId);

}
