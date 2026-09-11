package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.FormulaMetabolica;
import org.ironlog.app.model.LivelloAttivita;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProfiloMetabolicoResponseDTO {

    private Long id;
    private LocalDate calcolatoIl;
    private BigDecimal bmr;
    private BigDecimal tdee;
    private LivelloAttivita livelloAttivita;
    private FormulaMetabolica formula;
}
