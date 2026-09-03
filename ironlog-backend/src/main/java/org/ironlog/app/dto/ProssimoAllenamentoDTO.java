package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ProssimoAllenamentoDTO {

    private Long giornoSchedaId;
    private String giornoNome;
    private String schedaNome;
    private Integer numeroEsercizi;
}
