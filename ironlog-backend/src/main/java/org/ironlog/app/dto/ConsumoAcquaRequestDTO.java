package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ConsumoAcquaRequestDTO {

    @NotNull
    private LocalDate data;

    @NotNull
    @Positive
    private Integer mlConsumati;

    @Positive
    private Integer mlObiettivo;
}
