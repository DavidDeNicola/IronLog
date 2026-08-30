package org.ironlog.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.Ruolo;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Ruolo ruolo;
}
