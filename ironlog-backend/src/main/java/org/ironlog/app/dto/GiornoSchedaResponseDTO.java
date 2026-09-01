package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class GiornoSchedaResponseDTO {

    private String nome;
    private Integer ordine;
    private List<EsercizioSchedaResponseDTO> esercizi;

}
