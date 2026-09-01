package org.ironlog.app.repository;

import org.ironlog.app.model.Scheda;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchedaRepository extends JpaRepository<Scheda, Long> {

    List<Scheda> findByAtleta(Utente atleta);

    List<Scheda> findByAtletaAndAttivaTrue(Utente atleta);

    Optional<Scheda> findByIdAndAtleta(Long id, Utente atleta);

}
