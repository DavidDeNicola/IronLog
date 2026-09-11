package org.ironlog.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironlog.app.model.Sesso;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DatiBiometriciRequestDTO {

    @NotNull
    private Sesso sesso;

    @NotNull
    @Past
    private LocalDate dataNascita;
}
