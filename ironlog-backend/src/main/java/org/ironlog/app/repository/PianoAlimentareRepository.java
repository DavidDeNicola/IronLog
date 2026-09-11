package org.ironlog.app.repository;

import org.ironlog.app.model.Fase;
import org.ironlog.app.model.PianoAlimentare;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PianoAlimentareRepository extends JpaRepository<PianoAlimentare, Long> {

    List<PianoAlimentare> findByFaseOrderByDataGenerazioneDesc(Fase fase);

    Optional<PianoAlimentare> findByIdAndFaseAtleta(Long id, Utente atleta);
}
