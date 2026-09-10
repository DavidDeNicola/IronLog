package org.ironlog.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    private String password;

    @NotNull
    private Ruolo ruolo;

    /** Solo per gli atleti: coach da cui farsi seguire. Facoltativo. */
    private Long coachId;
}
