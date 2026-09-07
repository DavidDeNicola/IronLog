package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RiepilogoStatisticheDTO {

    private BigDecimal volumeTotale;
    private Integer serieTotali;
    private Integer numeroAllenamenti;
    private BigDecimal mediaPerAllenamento;
}