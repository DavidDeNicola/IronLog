package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "scheda")
public class Scheda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    private Boolean attiva;

    private LocalDate dataInizio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;

    @OneToMany(mappedBy = "scheda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordine ASC")
    private List<GiornoScheda> giorni = new ArrayList<>();


}
