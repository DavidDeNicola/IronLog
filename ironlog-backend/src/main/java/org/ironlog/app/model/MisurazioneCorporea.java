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
@Table(name = "misurazione_corporea")
public class MisurazioneCorporea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal pesoKg;

    @Column(nullable = false)
    private Integer altezzaCm;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualeMassaGrassa;

    @Column(length = 500)
    private String note;
}
