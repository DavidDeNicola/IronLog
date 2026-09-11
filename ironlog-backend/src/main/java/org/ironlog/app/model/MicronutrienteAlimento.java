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
@Table(name = "micronutriente_alimento")
public class MicronutrienteAlimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Column(length = 100, nullable = false)
    private String nomeNutriente;

    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal quantita;

    @Column(length = 20, nullable = false)
    private String unita;
}
