package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alimento")
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, unique = true, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FonteAlimento fonte;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal calorie100g;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal proteineG;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal grassiG;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal carboidratiG;

    @Column(precision = 6, scale = 2)
    private BigDecimal fibreG;

    @Column(precision = 6, scale = 2)
    private BigDecimal zuccheriG;

    @Column(precision = 7, scale = 2)
    private BigDecimal sodioMg;

    @OneToMany(mappedBy = "alimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MicronutrienteAlimento> micronutrienti = new ArrayList<>();
}
