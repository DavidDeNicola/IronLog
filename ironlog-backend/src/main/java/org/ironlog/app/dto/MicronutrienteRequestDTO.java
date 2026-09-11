package org.ironlog.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MicronutrienteRequestDTO {

    @NotBlank
    private String nomeNutriente;

    @NotNull
    private BigDecimal quantita;

    @NotBlank
    private String unita;
}
