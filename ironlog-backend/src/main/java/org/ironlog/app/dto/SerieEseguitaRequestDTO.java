package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class SerieEseguitaRequestDTO {

    private Long esercizioSchedaId;

    @NotNull
    private Long esercizioId;

    @NotNull
    @Positive
    private Integer ripetizioni;

    @NotNull
    @PositiveOrZero
    private BigDecimal peso;
}
