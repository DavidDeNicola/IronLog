package org.ironlog.app.service.definition;

import org.ironlog.app.dto.ConsumoAcquaRequestDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.model.Utente;

import java.time.LocalDate;
import java.util.List;

public interface ConsumoAcquaService {

    ConsumoAcquaResponseDTO registra(ConsumoAcquaRequestDTO dto, Utente atleta);

    List<ConsumoAcquaResponseDTO> findByAtletaEData(Utente atleta, LocalDate data);
}
