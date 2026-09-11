package org.ironlog.app.service.definition;

import org.ironlog.app.dto.AssunzioneIntegratoreRequestDTO;
import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.model.Utente;

import java.time.LocalDate;
import java.util.List;

public interface AssunzioneIntegratoreService {

    AssunzioneIntegratoreResponseDTO registra(AssunzioneIntegratoreRequestDTO dto, Utente atleta);

    List<AssunzioneIntegratoreResponseDTO> findByAtletaEData(Utente atleta, LocalDate data);
}
