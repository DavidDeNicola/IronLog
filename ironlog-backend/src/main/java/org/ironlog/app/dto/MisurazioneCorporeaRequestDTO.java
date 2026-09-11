package org.ironlog.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MisurazioneCorporeaRequestDTO {

    @NotNull
    private LocalDate data;

    @NotNull
    @Positive
    private BigDecimal pesoKg;

    @NotNull
    @Positive
    private Integer altezzaCm;

    @DecimalMin(value = "0", message = "La percentuale di massa grassa non può essere negativa")
    private BigDecimal percentualeMassaGrassa;

    private String note;
}
