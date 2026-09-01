package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UtenteResponseDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
}
