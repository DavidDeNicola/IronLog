package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Un ingrediente della ricetta di un piatto, con la quantità di riferimento
 * (porzione base) usata come punto di partenza per la scalatura proporzionale.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ingrediente_ricetta")
public class IngredienteRicetta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piatto_id", nullable = false)
    private Piatto piatto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal quantitaGrammi;
}
