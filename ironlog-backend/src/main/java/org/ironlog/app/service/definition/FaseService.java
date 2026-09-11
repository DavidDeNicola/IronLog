package org.ironlog.app.service.definition;

import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface FaseService {

    FaseResponseDTO create(FaseRequestDTO dto, Utente atleta);

    FaseResponseDTO findAttiva(Utente atleta);

    List<FaseSintesiDTO> findByAtleta(Utente atleta);

    FaseResponseDTO chiudi(Long id, Utente atleta);
}
