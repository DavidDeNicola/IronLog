package org.ironlog.app.mapper;

import org.ironlog.app.dto.IntegratoreRequestDTO;
import org.ironlog.app.dto.IntegratoreResponseDTO;
import org.ironlog.app.model.Integratore;
import org.springframework.stereotype.Component;

@Component
public class IntegratoreMapper {

    public Integratore toEntity(IntegratoreRequestDTO dto) {
        Integratore integratore = new Integratore();
        integratore.setNome(dto.getNome());
        integratore.setDosaggioDefault(dto.getDosaggioDefault());
        integratore.setUnita(dto.getUnita());
        return integratore;
    }

    public IntegratoreResponseDTO toResponseDTO(Integratore integratore) {
        IntegratoreResponseDTO dto = new IntegratoreResponseDTO();
        dto.setId(integratore.getId());
        dto.setNome(integratore.getNome());
        dto.setDosaggioDefault(integratore.getDosaggioDefault());
        dto.setUnita(integratore.getUnita());
        return dto;
    }
}
