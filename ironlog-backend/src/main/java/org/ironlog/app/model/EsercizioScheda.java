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
@Table(name = "esercizio_scheda", uniqueConstraints = @UniqueConstraint(columnNames = {"giorno_id", "ordine"}))
public class EsercizioScheda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giorno_id", nullable = false)
    private GiornoScheda giornoScheda;

    @Column(nullable = false)
    private Integer ordine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esercizio_id", nullable = false)
    private Esercizio esercizio;

    @Column(nullable = false)
    private Integer serie;

    @Column(nullable = false)
    private Integer ripetizioni;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal pesoAttuale;

    private Integer recupero;
}
