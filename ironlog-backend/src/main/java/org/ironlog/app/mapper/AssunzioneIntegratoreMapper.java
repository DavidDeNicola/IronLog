package org.ironlog.app.mapper;

import org.ironlog.app.dto.AssunzioneIntegratoreRequestDTO;
import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.model.AssunzioneIntegratore;
import org.ironlog.app.model.Integratore;
import org.ironlog.app.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class AssunzioneIntegratoreMapper {

    public AssunzioneIntegratore toEntity(AssunzioneIntegratoreRequestDTO dto, Utente atleta, Integratore integratore) {
        AssunzioneIntegratore assunzione = new AssunzioneIntegratore();
        assunzione.setAtleta(atleta);
        assunzione.setIntegratore(integratore);
        assunzione.setData(dto.getData());
        assunzione.setDosaggioAssunto(dto.getDosaggioAssunto());
        return assunzione;
    }

    public AssunzioneIntegratoreResponseDTO toResponseDTO(AssunzioneIntegratore assunzione) {
        AssunzioneIntegratoreResponseDTO dto = new AssunzioneIntegratoreResponseDTO();
        dto.setId(assunzione.getId());
        dto.setIntegratoreId(assunzione.getIntegratore().getId());
        dto.setIntegratoreNome(assunzione.getIntegratore().getNome());
        dto.setData(assunzione.getData());
        dto.setDosaggioAssunto(assunzione.getDosaggioAssunto());
        return dto;
    }
}
