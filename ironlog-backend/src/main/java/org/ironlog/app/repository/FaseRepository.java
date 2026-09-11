package org.ironlog.app.repository;

import org.ironlog.app.model.Fase;
import org.ironlog.app.model.TipoFase;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaseRepository extends JpaRepository<Fase, Long> {

    List<Fase> findByAtletaOrderByDataInizioDesc(Utente atleta);

    Optional<Fase> findByAtletaAndDataFineIsNull(Utente atleta);

    Optional<Fase> findByIdAndAtleta(Long id, Utente atleta);

    Optional<Fase> findFirstByAtletaAndTipoAndDataFineIsNotNullOrderByDataFineDesc(Utente atleta, TipoFase tipo);

    Optional<Fase> findFirstByAtletaAndDataFineIsNotNullOrderByDataFineDesc(Utente atleta);
}
