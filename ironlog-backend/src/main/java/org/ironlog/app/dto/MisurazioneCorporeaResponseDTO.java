package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MisurazioneCorporeaResponseDTO {

    private Long id;
    private LocalDate data;
    private BigDecimal pesoKg;
    private Integer altezzaCm;
    private BigDecimal percentualeMassaGrassa;
    private String note;
}
