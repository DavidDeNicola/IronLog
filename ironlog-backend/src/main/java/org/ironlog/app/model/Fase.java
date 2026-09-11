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
@Table(name = "fase")
public class Fase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profilo_metabolico_riferimento_id", nullable = false)
    private ProfiloMetabolico profiloMetabolicoRiferimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFase tipo;

    @Column(nullable = false)
    private LocalDate dataInizio;

    private LocalDate dataFine;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal baseTargetCalorico;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal targetProteineG;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal targetGrassiG;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal targetCarboidratiG;
}
