package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class SerieEseguitaResponseDTO {

    private Long id;
    private Long esercizioId;
    private Integer numeroSerie;
    private String esercizioNome;
    private Integer ripetizioni;
    private BigDecimal peso;
    private Integer ripObiettivo;
    private BigDecimal massimaleStimato;
}
