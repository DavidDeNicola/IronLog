package org.ironlog.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Un piatto reale (es. "Pollo, riso e broccoli") composto da una ricetta di
 * alimenti in proporzioni fisse. Il motore di generazione dieta sceglie un
 * piatto per pasto e lo scala proporzionalmente per avvicinarsi al target
 * calorico/macro residuo, invece di comporre alimenti sciolti.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "piatto")
public class Piatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, unique = true, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "piatto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PiattoPasto> pastiIdonei = new ArrayList<>();

    @OneToMany(mappedBy = "piatto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredienteRicetta> ingredienti = new ArrayList<>();
}
