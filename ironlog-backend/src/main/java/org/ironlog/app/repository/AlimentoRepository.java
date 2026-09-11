package org.ironlog.app.repository;

import org.ironlog.app.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    List<Alimento> findAllByOrderByNomeAsc();

    Optional<Alimento> findByNomeIgnoreCase(String nome);

    @Query("SELECT a FROM Alimento a WHERE " +
            "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "ORDER BY a.nome ASC")
    List<Alimento> cerca(@Param("nome") String nome);
}
