package org.ironlog.app.repository;

import org.ironlog.app.model.GiornoScheda;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiornoSchedaRepository extends JpaRepository<GiornoScheda, Long> {

    Optional<GiornoScheda> findByIdAndSchedaAtleta(Long id, Utente atleta);
}
