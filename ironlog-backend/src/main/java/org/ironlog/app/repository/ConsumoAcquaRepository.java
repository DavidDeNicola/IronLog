package org.ironlog.app.repository;

import org.ironlog.app.model.ConsumoAcqua;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ConsumoAcquaRepository extends JpaRepository<ConsumoAcqua, Long> {

    List<ConsumoAcqua> findByAtletaAndData(Utente atleta, LocalDate data);

    List<ConsumoAcqua> findByAtleta(Utente atleta);
}
