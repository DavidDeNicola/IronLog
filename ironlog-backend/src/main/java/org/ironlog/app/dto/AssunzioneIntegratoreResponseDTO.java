package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AssunzioneIntegratoreResponseDTO {

    private Long id;
    private Long integratoreId;
    private String integratoreNome;
    private LocalDate data;
    private BigDecimal dosaggioAssunto;
}
