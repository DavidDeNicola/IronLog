package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Associa un piatto ai tipi di pasto per cui è idoneo (un piatto salato può
 * andare bene sia per pranzo che per cena, un piatto dolce solo a colazione).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "piatto_pasto")
public class PiattoPasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piatto_id", nullable = false)
    private Piatto piatto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPasto tipoPasto;
}
