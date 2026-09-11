package org.ironlog.app.repository;

import org.ironlog.app.model.Integratore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface IntegratoreRepository extends JpaRepository<Integratore, Long> {

    List<Integratore> findAllByOrderByNomeAsc();

    Optional<Integratore> findByNomeIgnoreCase(String nome);
}
