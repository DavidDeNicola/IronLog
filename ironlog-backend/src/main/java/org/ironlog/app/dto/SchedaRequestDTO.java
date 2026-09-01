package org.ironlog.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SchedaRequestDTO {

    @NotBlank
    private String nome;

    private String note;

    @NotNull
    private LocalDate dataInizio;

    @NotEmpty
    @Valid
    private List<GiornoSchedaRequestDTO> giorni;
}
