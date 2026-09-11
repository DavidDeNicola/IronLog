package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IntegratoreResponseDTO {

    private Long id;
    private String nome;
    private BigDecimal dosaggioDefault;
    private String unita;
}
