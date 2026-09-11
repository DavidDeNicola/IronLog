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
@Table(name = "integratore")
public class Integratore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, unique = true, nullable = false)
    private String nome;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal dosaggioDefault;

    @Column(length = 20, nullable = false)
    private String unita;
}
