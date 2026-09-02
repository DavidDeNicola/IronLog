package org.ironlog.app.repository;

import org.ironlog.app.model.Esercizio;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SerieEseguitaRepository extends JpaRepository<SerieEseguita, Long> {

    @Query("SELECT s FROM SerieEseguita s WHERE s.esercizio = :esercizio " +
            "AND s.sessione.atleta = :atleta ORDER BY s.sessione.eseguitaIl DESC")
    List<SerieEseguita> storicoEsercizio(@Param("esercizio") Esercizio esercizio,
                                         @Param("atleta") Utente atleta);

    List<SerieEseguita> findBySessioneAtletaAndSessioneEseguitaIlBetween(Utente atleta, LocalDateTime da, LocalDateTime a);
}
