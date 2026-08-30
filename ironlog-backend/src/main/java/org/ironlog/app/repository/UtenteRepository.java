package org.ironlog.app.repository;

import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    List <Utente> findByCoach(Utente coach);

    boolean existsByEmail(String email);
}
