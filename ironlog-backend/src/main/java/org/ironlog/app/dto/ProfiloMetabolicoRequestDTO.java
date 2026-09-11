package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.FormulaMetabolica;
import org.ironlog.app.model.LivelloAttivita;

@Getter
@Setter
@NoArgsConstructor
public class ProfiloMetabolicoRequestDTO {

    @NotNull
    private LivelloAttivita livelloAttivita;

    private FormulaMetabolica formulaForzata;
}
