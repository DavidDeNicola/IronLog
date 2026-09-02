package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class RiepilogoSessioneDTO {

    private Long sessioneId;
    private LocalDateTime eseguitaIl;
    private LocalDateTime conclusaIl;
    private Long durataMinuti;
    private Integer numeroSerie;
    private BigDecimal volumeTotale;
    private Integer serieCompletate;
}
