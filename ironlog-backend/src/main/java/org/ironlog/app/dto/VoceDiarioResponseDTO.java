package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoPasto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VoceDiarioResponseDTO {

    private Long id;
    private Long alimentoId;
    private String alimentoNome;
    private LocalDate data;
    private TipoPasto tipoPasto;
    private BigDecimal quantitaGrammi;
    private BigDecimal calorie;
    private BigDecimal proteineG;
    private BigDecimal grassiG;
    private BigDecimal carboidratiG;
}
