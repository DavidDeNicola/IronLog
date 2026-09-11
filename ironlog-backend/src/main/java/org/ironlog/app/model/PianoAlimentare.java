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
@Table(name = "piano_alimentare")
public class PianoAlimentare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fase_id", nullable = false)
    private Fase fase;

    @Column(nullable = false)
    private LocalDate dataGenerazione;

    @Column(nullable = false)
    private Boolean vincoliSoddisfatti;

    @OneToMany(mappedBy = "pianoAlimentare", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<VocePianoAlimentare> voci = new ArrayList<>();
}
