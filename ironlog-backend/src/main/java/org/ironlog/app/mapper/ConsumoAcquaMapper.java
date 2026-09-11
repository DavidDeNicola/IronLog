package org.ironlog.app.mapper;

import org.ironlog.app.dto.ConsumoAcquaRequestDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.model.ConsumoAcqua;
import org.ironlog.app.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class ConsumoAcquaMapper {

    public ConsumoAcqua toEntity(ConsumoAcquaRequestDTO dto, Utente atleta) {
        ConsumoAcqua consumo = new ConsumoAcqua();
        consumo.setAtleta(atleta);
        consumo.setData(dto.getData());
        consumo.setMlConsumati(dto.getMlConsumati());
        consumo.setMlObiettivo(dto.getMlObiettivo());
        return consumo;
    }

    public ConsumoAcquaResponseDTO toResponseDTO(ConsumoAcqua consumo) {
        ConsumoAcquaResponseDTO dto = new ConsumoAcquaResponseDTO();
        dto.setId(consumo.getId());
        dto.setData(consumo.getData());
        dto.setMlConsumati(consumo.getMlConsumati());
        dto.setMlObiettivo(consumo.getMlObiettivo());
        return dto;
    }
}
