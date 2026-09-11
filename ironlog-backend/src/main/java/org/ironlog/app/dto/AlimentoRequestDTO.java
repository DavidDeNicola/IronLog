package org.ironlog.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AlimentoRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @PositiveOrZero
    private BigDecimal calorie100g;

    @NotNull
    @PositiveOrZero
    private BigDecimal proteineG;

    @NotNull
    @PositiveOrZero
    private BigDecimal grassiG;

    @NotNull
    @PositiveOrZero
    private BigDecimal carboidratiG;

    @PositiveOrZero
    private BigDecimal fibreG;

    @PositiveOrZero
    private BigDecimal zuccheriG;

    @PositiveOrZero
    private BigDecimal sodioMg;

    @Valid
    private List<MicronutrienteRequestDTO> micronutrienti;
}
