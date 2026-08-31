package org.ironlog.app.service.definition;

import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;

import java.util.List;

public interface CatalogoService {

    List<GruppoMuscolareResponseDTO> findAllGruppi();
    List<EsercizioResponseDTO> findAllEsercizi();
    List<EsercizioResponseDTO> findAllEserciziByGruppo(Long gruppoMuscolareId);
}
