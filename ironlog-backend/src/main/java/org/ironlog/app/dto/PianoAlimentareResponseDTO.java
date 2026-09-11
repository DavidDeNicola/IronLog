package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PianoAlimentareResponseDTO {

    private Long id;
    private Long faseId;
    private LocalDate dataGenerazione;
    private boolean vincoliSoddisfatti;
    private List<VocePianoAlimentareResponseDTO> voci;
    private BigDecimal calorieTotali;
    private BigDecimal proteineTotali;
    private BigDecimal grassiTotali;
    private BigDecimal carboidratiTotali;
}
