package org.ironlog.app.repository;

import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SerieEseguitaRepository extends JpaRepository<SerieEseguita, Long> {

    List<SerieEseguita> findBySessioneAtletaAndSessioneEseguitaIlBetween(Utente atleta, LocalDateTime da, LocalDateTime a);
}
