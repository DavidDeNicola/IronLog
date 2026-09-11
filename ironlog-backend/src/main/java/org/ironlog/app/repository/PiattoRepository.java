package org.ironlog.app.repository;

import org.ironlog.app.model.Piatto;
import org.ironlog.app.model.TipoPasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PiattoRepository extends JpaRepository<Piatto, Long> {

    @Query("""
            select distinct p from Piatto p
            join p.pastiIdonei pp
            left join fetch p.ingredienti ing
            left join fetch ing.alimento
            where pp.tipoPasto = :tipoPasto
            """)
    List<Piatto> findByTipoPastoConIngredienti(@Param("tipoPasto") TipoPasto tipoPasto);
}
