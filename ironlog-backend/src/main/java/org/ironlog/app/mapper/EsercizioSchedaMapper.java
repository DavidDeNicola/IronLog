package org.ironlog.app.mapper;

import org.ironlog.app.dto.EsercizioSchedaRequestDTO;
import org.ironlog.app.dto.EsercizioSchedaResponseDTO;
import org.ironlog.app.model.Esercizio;
import org.ironlog.app.model.EsercizioScheda;
import org.ironlog.app.model.GiornoScheda;
import org.springframework.stereotype.Component;

@Component
public class EsercizioSchedaMapper {

    public EsercizioScheda toEntity(EsercizioSchedaRequestDTO dto, Esercizio esercizio, int ordine, GiornoScheda giornoScheda){
        EsercizioScheda esercizioScheda = new EsercizioScheda();
        esercizioScheda.setEsercizio(esercizio);
        esercizioScheda.setOrdine(ordine);
        esercizioScheda.setGiornoScheda(giornoScheda);
        esercizioScheda.setSerie(dto.getSerie());
        esercizioScheda.setRipetizioni(dto.getRipetizioni());
        esercizioScheda.setPesoAttuale(dto.getPesoAttuale());
        esercizioScheda.setRecupero(dto.getRecupero());
        return esercizioScheda;
    }

    public EsercizioSchedaResponseDTO toResponseDTO(EsercizioScheda esercizioScheda){
        EsercizioSchedaResponseDTO dto = new EsercizioSchedaResponseDTO();
        dto.setId(esercizioScheda.getId());
        dto.setEsercizioNome(esercizioScheda.getEsercizio().getNome());
        dto.setOrdine(esercizioScheda.getOrdine());
        dto.setSerie(esercizioScheda.getSerie());
        dto.setRipetizioni(esercizioScheda.getRipetizioni());
        dto.setPesoAttuale(esercizioScheda.getPesoAttuale());
        dto.setRecupero(esercizioScheda.getRecupero());
        return dto;
    }
}
