package it.polito.ai.esercitazione3.repositories;

import it.polito.ai.esercitazione3.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
