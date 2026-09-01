package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class SchedaResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataInizio;
    private Boolean attiva;
    private Integer numeroGiorni;
    private String note;
    private String atletaNome;
    private String autoreNome;
    private List<GiornoSchedaResponseDTO> giorni;
}
