package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class EsercizioSchedaResponseDTO {

    private Long id;
    private String esercizioNome;
    private Integer ordine;
    private Integer serie;
    private Integer ripetizioni;
    private BigDecimal pesoAttuale;
    private Integer recupero;
}
