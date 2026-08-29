package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "giorno_scheda", uniqueConstraints = @UniqueConstraint(columnNames = {"scheda_id", "ordine"}))
public class GiornoScheda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer ordine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheda_id", nullable = false)
    private Scheda scheda;

    @OneToMany(mappedBy = "giornoScheda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordine ASC")
    private List<EsercizioScheda> esercizi = new ArrayList<>();
}
