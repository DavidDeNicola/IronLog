package org.ironlog.app.repository;

import org.ironlog.app.model.EsercizioScheda;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EsercizioSchedaRepository extends JpaRepository<EsercizioScheda, Long> {

    Optional<EsercizioScheda> findByIdAndGiornoSchedaSchedaAtleta(Long id, Utente atleta);
}
