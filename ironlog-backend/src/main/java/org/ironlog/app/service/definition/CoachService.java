package org.ironlog.app.service.definition;

import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface CoachService {

    List<UtenteResponseDTO> findAtleti(Utente coach);

    SchedaResponseDTO createSchedaPerAtleta(SchedaRequestDTO dto, Long atletaId, Utente coach);

    List<SchedaSintesiDTO> findSchedeAtleta(Long atletaId, Utente coach);
}
