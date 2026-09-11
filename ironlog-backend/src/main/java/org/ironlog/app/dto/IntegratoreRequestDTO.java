package org.ironlog.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IntegratoreRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private BigDecimal dosaggioDefault;

    @NotBlank
    private String unita;
}
