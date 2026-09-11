package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ConsumoAcquaResponseDTO {

    private Long id;
    private LocalDate data;
    private Integer mlConsumati;
    private Integer mlObiettivo;
}
