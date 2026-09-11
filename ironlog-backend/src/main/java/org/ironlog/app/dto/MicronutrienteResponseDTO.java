package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MicronutrienteResponseDTO {

    private String nomeNutriente;
    private BigDecimal quantita;
    private String unita;
}
