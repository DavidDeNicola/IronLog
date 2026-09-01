package org.ironlog.app.service.definition;

import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface SchedaService {

    SchedaResponseDTO create(SchedaRequestDTO schedaRequestDTO, Utente atleta, Utente autore);

    List<SchedaSintesiDTO> findByAtleta(Utente atleta);

    SchedaResponseDTO findByIdAndAtleta(Long id, Utente atleta);
}
