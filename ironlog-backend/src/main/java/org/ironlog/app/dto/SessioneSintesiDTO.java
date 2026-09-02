package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class SessioneSintesiDTO {

    private Long id;
    private LocalDateTime eseguitaIl;
    private LocalDateTime conclusaIl;
    private String giornoNome;
    private Integer numeroSerie;
}
