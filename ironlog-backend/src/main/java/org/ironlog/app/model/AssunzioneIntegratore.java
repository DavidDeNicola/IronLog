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
@Table(name = "assunzione_integratore")
public class AssunzioneIntegratore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integratore_id", nullable = false)
    private Integratore integratore;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal dosaggioAssunto;
}
