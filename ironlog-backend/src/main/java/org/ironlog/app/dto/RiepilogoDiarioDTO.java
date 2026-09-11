package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RiepilogoDiarioDTO {

    private LocalDate data;
    private BigDecimal calorieTotali;
    private BigDecimal proteineTotali;
    private BigDecimal grassiTotali;
    private BigDecimal carboidratiTotali;
    private BigDecimal targetCalorico;
    private BigDecimal differenzaCalorica;
}
