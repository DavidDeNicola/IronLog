package org.ironlog.app.mapper;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.GiornoSchedaRequestDTO;
import org.ironlog.app.dto.GiornoSchedaResponseDTO;
import org.ironlog.app.model.GiornoScheda;
import org.ironlog.app.model.Scheda;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiornoSchedaMapper {

    private final EsercizioSchedaMapper esercizioSchedaMapper;

    public GiornoScheda toEntity(GiornoSchedaRequestDTO dto, int ordine, Scheda scheda){
        GiornoScheda giornoScheda = new GiornoScheda();
        giornoScheda.setNome(dto.getNome());
        giornoScheda.setScheda(scheda);
        giornoScheda.setOrdine(ordine);
        return giornoScheda;
    }

    public GiornoSchedaResponseDTO toResponseDTO(GiornoScheda giornoScheda){
        GiornoSchedaResponseDTO dto = new GiornoSchedaResponseDTO();
        dto.setId(giornoScheda.getId());
        dto.setNome(giornoScheda.getNome());
        dto.setOrdine(giornoScheda.getOrdine());
        dto.setEsercizi(giornoScheda.getEsercizi().stream().map(esercizioSchedaMapper::toResponseDTO).toList());
        return dto;
    }
}
