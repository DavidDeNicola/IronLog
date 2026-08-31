package org.ironlog.app.mapper;

import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.model.Esercizio;
import org.springframework.stereotype.Component;

@Component

public class EsercizioMapper {

    public EsercizioResponseDTO toResponseDTO(Esercizio esercizio) {
        EsercizioResponseDTO responseDTO = new EsercizioResponseDTO();
        responseDTO.setId(esercizio.getId());
        responseDTO.setNome(esercizio.getNome());
        responseDTO.setDescrizione(esercizio.getDescrizione());
        responseDTO.setGruppoMuscolareNome(esercizio.getGruppoMuscolare().getNome());
        return responseDTO;
    }
}
