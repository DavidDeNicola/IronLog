package org.ironlog.app.repository;

import org.ironlog.app.model.MisurazioneCorporea;
import org.ironlog.app.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MisurazioneCorporeaRepository extends JpaRepository<MisurazioneCorporea, Long> {

    List<MisurazioneCorporea> findByAtletaOrderByDataDesc(Utente atleta);

    Optional<MisurazioneCorporea> findFirstByAtletaOrderByDataDesc(Utente atleta);

    Optional<MisurazioneCorporea> findByIdAndAtleta(Long id, Utente atleta);
}
