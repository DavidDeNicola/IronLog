package org.ironlog.app.repository;

import org.ironlog.app.model.Utente;
import org.ironlog.app.model.VoceDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoceDiarioRepository extends JpaRepository<VoceDiario, Long> {

    List<VoceDiario> findByAtletaAndData(Utente atleta, LocalDate data);

    Optional<VoceDiario> findByIdAndAtleta(Long id, Utente atleta);

    List<VoceDiario> findByAtletaAndDataBetween(Utente atleta, LocalDate dataInizio, LocalDate dataFine);
}
