package org.ironlog.app.service.definition;

import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface PianoAlimentareService {

    PianoAlimentareResponseDTO genera(Long faseId, Utente atleta);

    List<PianoAlimentareSintesiDTO> findByFase(Long faseId, Utente atleta);

    PianoAlimentareResponseDTO findById(Long id, Utente atleta);
}
