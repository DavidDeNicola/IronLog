package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class SessioneResponseDTO {
    private Long id;
    private LocalDateTime eseguitaIl;
    private LocalDateTime conclusaIl;
    private String giornoNome;
    private String schedaNome;
    private String note;
    private List<SerieEseguitaResponseDTO> serie;
}
