package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.FonteAlimento;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AlimentoResponseDTO {

    private Long id;
    private String nome;
    private FonteAlimento fonte;
    private BigDecimal calorie100g;
    private BigDecimal proteineG;
    private BigDecimal grassiG;
    private BigDecimal carboidratiG;
    private BigDecimal fibreG;
    private BigDecimal zuccheriG;
    private BigDecimal sodioMg;
    private List<MicronutrienteResponseDTO> micronutrienti;
}
