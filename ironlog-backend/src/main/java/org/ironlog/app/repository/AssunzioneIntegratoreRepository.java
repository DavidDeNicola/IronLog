package org.ironlog.app.repository;

import org.ironlog.app.model.AssunzioneIntegratore;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AssunzioneIntegratoreRepository extends JpaRepository<AssunzioneIntegratore, Long> {

    List<AssunzioneIntegratore> findByAtletaAndData(Utente atleta, LocalDate data);
}
