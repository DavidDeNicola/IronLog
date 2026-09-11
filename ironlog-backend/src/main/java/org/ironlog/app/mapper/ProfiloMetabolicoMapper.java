package org.ironlog.app.mapper;

import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.model.ProfiloMetabolico;
import org.springframework.stereotype.Component;

@Component
public class ProfiloMetabolicoMapper {

    public ProfiloMetabolicoResponseDTO toResponseDTO(ProfiloMetabolico profilo) {
        ProfiloMetabolicoResponseDTO dto = new ProfiloMetabolicoResponseDTO();
        dto.setId(profilo.getId());
        dto.setCalcolatoIl(profilo.getCalcolatoIl());
        dto.setBmr(profilo.getBmr());
        dto.setTdee(profilo.getTdee());
        dto.setLivelloAttivita(profilo.getLivelloAttivita());
        dto.setFormula(profilo.getFormula());
        return dto;
    }
}
