package org.ironlog.app.dto;

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
public class AssunzioneIntegratoreRequestDTO {

    @NotNull
    private Long integratoreId;

    @NotNull
    private LocalDate data;

    @NotNull
    @Positive
    private BigDecimal dosaggioAssunto;
}
