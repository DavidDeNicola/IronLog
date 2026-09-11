package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoFase;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FaseRequestDTO {

    @NotNull
    private TipoFase tipo;

    @NotNull
    private LocalDate dataInizio;

    @Positive
    private BigDecimal targetCaloricoOverride;

    @Positive
    private BigDecimal targetProteineGOverride;

    @Positive
    private BigDecimal targetGrassiGOverride;

    @Positive
    private BigDecimal targetCarboidratiGOverride;
}
