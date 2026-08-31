package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EsercizioResponseDTO {

    private Long id;
    private String nome;
    private String descrizione;
    private String gruppoMuscolareNome;
}
