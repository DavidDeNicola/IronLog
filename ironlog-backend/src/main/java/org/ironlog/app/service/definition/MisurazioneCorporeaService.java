package org.ironlog.app.service.definition;

import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface MisurazioneCorporeaService {

    MisurazioneCorporeaResponseDTO registra(MisurazioneCorporeaRequestDTO dto, Utente atleta);

    List<MisurazioneCorporeaResponseDTO> findByAtleta(Utente atleta);

    MisurazioneCorporeaResponseDTO findUltima(Utente atleta);
}
