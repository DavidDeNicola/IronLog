package org.ironlog.app.repository;

import org.ironlog.app.model.Sessione;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessioneRepository extends JpaRepository<Sessione, Long> {

    List<Sessione> findByAtletaOrderByEseguitaIlDesc(Utente atleta);

    List<Sessione> findByAtletaAndEseguitaIlBetweenOrderByEseguitaIlDesc(Utente atleta, LocalDateTime da, LocalDateTime a);

    List<Sessione> findByAtletaAndConclusaIlNull(Utente atleta);

    Optional<Sessione> findByIdAndAtleta(Long id, Utente atleta);

}
