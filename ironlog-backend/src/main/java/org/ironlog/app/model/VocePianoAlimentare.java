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
@Table(name = "voce_piano_alimentare")
public class VocePianoAlimentare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piano_alimentare_id", nullable = false)
    private PianoAlimentare pianoAlimentare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPasto tipoPasto;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal quantitaGrammi;
}
