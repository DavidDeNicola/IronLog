package org.ironlog.app.mapper;

import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.model.MisurazioneCorporea;
import org.ironlog.app.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class MisurazioneCorporeaMapper {

    public MisurazioneCorporea toEntity(MisurazioneCorporeaRequestDTO dto, Utente atleta) {
        MisurazioneCorporea misurazione = new MisurazioneCorporea();
        misurazione.setAtleta(atleta);
        misurazione.setData(dto.getData());
        misurazione.setPesoKg(dto.getPesoKg());
        misurazione.setAltezzaCm(dto.getAltezzaCm());
        misurazione.setPercentualeMassaGrassa(dto.getPercentualeMassaGrassa());
        misurazione.setNote(dto.getNote());
        return misurazione;
    }

    public MisurazioneCorporeaResponseDTO toResponseDTO(MisurazioneCorporea misurazione) {
        MisurazioneCorporeaResponseDTO dto = new MisurazioneCorporeaResponseDTO();
        dto.setId(misurazione.getId());
        dto.setData(misurazione.getData());
        dto.setPesoKg(misurazione.getPesoKg());
        dto.setAltezzaCm(misurazione.getAltezzaCm());
        dto.setPercentualeMassaGrassa(misurazione.getPercentualeMassaGrassa());
        dto.setNote(misurazione.getNote());
        return dto;
    }
}
