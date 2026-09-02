package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sessione")
public class Sessione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Utente atleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giorno_id")  //facoltativa, deve essere possibile registare un allenamento libero fuori scheda
    private GiornoScheda giornoScheda;

    @Column(nullable = false)
    private LocalDateTime eseguitaIl;

    private LocalDateTime conclusaIl;

    @Column(length =  500)
    private String note;

    @OneToMany(mappedBy = "sessione", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroSerie ASC")
    private List<SerieEseguita> serie = new ArrayList<>();


}
