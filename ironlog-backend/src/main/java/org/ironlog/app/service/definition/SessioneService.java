package org.ironlog.app.service.definition;

import org.ironlog.app.dto.*;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface SessioneService {

    SessioneResponseDTO apri(SessioneRequestDTO dto, Utente atleta);

    SerieEseguitaResponseDTO registraSerie(Long sessioneId, SerieEseguitaRequestDTO dto, Utente atleta);

    RiepilogoSessioneDTO concludi(Long sessioneId, Utente atleta);

    SessioneResponseDTO findAperta(Utente atleta);

    List<SessioneSintesiDTO> findStorico(Utente atleta);
}
