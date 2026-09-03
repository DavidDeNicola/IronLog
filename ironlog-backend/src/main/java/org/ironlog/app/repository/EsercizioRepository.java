package org.ironlog.app.repository;

import org.ironlog.app.model.Esercizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EsercizioRepository extends JpaRepository<Esercizio, Long> {

    List<Esercizio> findByGruppoMuscolareIdOrderByNomeAsc(Long gruppoMuscolareId);

    List<Esercizio> findAllByOrderByNomeAsc();

    List<Esercizio> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    @Query("SELECT e FROM Esercizio e WHERE " +
            "(:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:gruppoId IS NULL OR e.gruppoMuscolare.id = :gruppoId) " +
            "ORDER BY e.nome ASC")
    List<Esercizio> cerca(@Param("nome") String nome, @Param("gruppoId") Long gruppoId);
}
