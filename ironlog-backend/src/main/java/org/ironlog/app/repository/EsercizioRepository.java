package org.ironlog.app.repository;

import org.ironlog.app.model.Esercizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EsercizioRepository extends JpaRepository<Esercizio, Long> {

    List<Esercizio> findByGruppoMuscolareIdOrderByNomeAsc(Long gruppoMuscolareId);

    List<Esercizio> findAllByOrderByNomeAsc();
}
