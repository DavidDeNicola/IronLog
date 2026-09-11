package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoFase;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FaseResponseDTO {

    private Long id;
    private TipoFase tipo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private BigDecimal baseTargetCalorico;
    private BigDecimal targetCaloricoAttuale;
    private BigDecimal targetProteineG;
    private BigDecimal targetGrassiG;
    private BigDecimal targetCarboidratiG;
    private Integer durataSuggeritaMesi;
    private long giorniTrascorsi;
}
