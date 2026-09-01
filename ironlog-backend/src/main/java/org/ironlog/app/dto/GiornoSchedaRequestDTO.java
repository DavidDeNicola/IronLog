package org.ironlog.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GiornoSchedaRequestDTO {

    @NotBlank
    private String nome;

    @NotEmpty
    @Valid
    private List<EsercizioSchedaRequestDTO> esercizi;

}
