package org.ironlog.app.service.definition;

import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface ProfiloMetabolicoService {

    ProfiloMetabolicoResponseDTO calcola(ProfiloMetabolicoRequestDTO dto, Utente atleta);

    ProfiloMetabolicoResponseDTO findAttuale(Utente atleta);

    List<ProfiloMetabolicoResponseDTO> findStorico(Utente atleta);
}
