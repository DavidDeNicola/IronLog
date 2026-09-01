package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class SchedaSintesiDTO {

    private Long id;
    private String nome;
    private LocalDate dataInizio;
    private Boolean attiva;
    private Integer numeroGiorni;

}
