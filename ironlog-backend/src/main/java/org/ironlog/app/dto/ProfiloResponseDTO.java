package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.Ruolo;
import org.ironlog.app.model.Sesso;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class ProfiloResponseDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private Ruolo ruolo;
    private Sesso sesso;
    private LocalDate dataNascita;
}
