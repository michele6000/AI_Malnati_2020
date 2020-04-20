package it.polito.ai.esercitazione2.repositories;

import it.polito.ai.esercitazione2.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
