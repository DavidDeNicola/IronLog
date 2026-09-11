package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "profilo_metabolico")
public class ProfiloMetabolico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "misurazione_riferimento_id", nullable = false)
    private MisurazioneCorporea misurazioneRiferimento;

    @Column(nullable = false)
    private LocalDate calcolatoIl;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal bmr;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal tdee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivelloAttivita livelloAttivita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormulaMetabolica formula;
}
