package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticaClienteDTO {

    private Long atletaId;
    private String nome;
    private String cognome;
    private int numeroSchede;
    private int allenamentiUltimi30Giorni;
    private String progressione; // IN_PROGRESSO, STABILE, IN_CALO, DATI_INSUFFICIENTI
}