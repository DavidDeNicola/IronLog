package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoPasto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VoceDiarioRequestDTO {

    @NotNull
    private Long alimentoId;

    @NotNull
    private LocalDate data;

    @NotNull
    private TipoPasto tipoPasto;

    @NotNull
    @Positive
    private BigDecimal quantitaGrammi;
}
