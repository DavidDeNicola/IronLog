package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class VolumeGruppoDTO {

    private String gruppoMuscolareNome;
    private BigDecimal volumeTotale;
    private Integer numeroSerie;
}
