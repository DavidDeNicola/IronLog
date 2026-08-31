package org.ironlog.app.mapper;

import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.model.GruppoMuscolare;
import org.springframework.stereotype.Component;

@Component

public class GruppoMuscolareMapper {

    public GruppoMuscolareResponseDTO toResponseDTO(GruppoMuscolare gruppoMuscolare){
        GruppoMuscolareResponseDTO responseDTO = new GruppoMuscolareResponseDTO();
        responseDTO.setId(gruppoMuscolare.getId());
        responseDTO.setNome(gruppoMuscolare.getNome());
        return responseDTO;
    }
}
