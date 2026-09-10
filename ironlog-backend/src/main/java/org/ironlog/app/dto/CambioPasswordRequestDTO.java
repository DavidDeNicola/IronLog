package org.ironlog.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CambioPasswordRequestDTO {

    @NotBlank
    private String passwordAttuale;

    @NotBlank
    @Size(min = 8, message = "La nuova password deve avere almeno 8 caratteri")
    private String nuovaPassword;
}