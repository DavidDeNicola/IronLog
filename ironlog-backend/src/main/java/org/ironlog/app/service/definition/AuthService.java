package org.ironlog.app.service.definition;

import org.ironlog.app.dto.LoginRequestDTO;
import org.ironlog.app.dto.RegisterRequestDTO;
import org.ironlog.app.dto.UtenteResponseDTO;

import java.util.List;

public interface AuthService {

    void register(RegisterRequestDTO dto);
    String login(LoginRequestDTO dto);

    /** Elenco dei coach disponibili, mostrato in registrazione. */
    List<UtenteResponseDTO> findCoachDisponibili();
}
