package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PianoAlimentareSintesiDTO {

    private Long id;
    private LocalDate dataGenerazione;
    private boolean vincoliSoddisfatti;
}
