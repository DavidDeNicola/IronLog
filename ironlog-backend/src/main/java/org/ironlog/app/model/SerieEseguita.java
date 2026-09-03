package org.ironlog.app.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "serie_eseguita", uniqueConstraints = @UniqueConstraint(columnNames = {"sessione_id", "numero_serie"}))
public class SerieEseguita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessione_id", nullable = false)
    private Sessione sessione;

    @Column(nullable = false)
    private Integer numeroSerie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esercizio_id", nullable = false)
    private Esercizio esercizio;

    @Column(nullable = false)
    private Integer ripetizioni;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal peso;

    private Integer ripObiettivo;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal massimaleStimato;

    private Integer rpe;
}
