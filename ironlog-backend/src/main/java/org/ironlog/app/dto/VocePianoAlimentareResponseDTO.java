package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoPasto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class VocePianoAlimentareResponseDTO {

    private Long alimentoId;
    private String alimentoNome;
    private TipoPasto tipoPasto;
    private BigDecimal quantitaGrammi;
    private BigDecimal calorie;
    private BigDecimal proteineG;
    private BigDecimal grassiG;
    private BigDecimal carboidratiG;
}
