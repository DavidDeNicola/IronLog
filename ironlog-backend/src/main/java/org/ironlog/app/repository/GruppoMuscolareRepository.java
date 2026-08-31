package org.ironlog.app.repository;

import org.ironlog.app.model.GruppoMuscolare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GruppoMuscolareRepository extends JpaRepository<GruppoMuscolare, Long> {

    List<GruppoMuscolare> findAllByOrderByNomeAsc();
}
