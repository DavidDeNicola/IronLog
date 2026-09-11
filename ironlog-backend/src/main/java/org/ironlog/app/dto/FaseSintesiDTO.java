package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.TipoFase;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FaseSintesiDTO {

    private Long id;
    private TipoFase tipo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private boolean attiva;
}
