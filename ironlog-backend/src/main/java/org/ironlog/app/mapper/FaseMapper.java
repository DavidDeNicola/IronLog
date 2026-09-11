package org.ironlog.app.mapper;

import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.model.Fase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FaseMapper {

    public FaseResponseDTO toResponseDTO(Fase fase, BigDecimal targetCaloricoAttuale, long giorniTrascorsi, Integer durataSuggeritaMesi) {
        FaseResponseDTO dto = new FaseResponseDTO();
        dto.setId(fase.getId());
        dto.setTipo(fase.getTipo());
        dto.setDataInizio(fase.getDataInizio());
        dto.setDataFine(fase.getDataFine());
        dto.setBaseTargetCalorico(fase.getBaseTargetCalorico());
        dto.setTargetCaloricoAttuale(targetCaloricoAttuale);
        dto.setTargetProteineG(fase.getTargetProteineG());
        dto.setTargetGrassiG(fase.getTargetGrassiG());
        dto.setTargetCarboidratiG(fase.getTargetCarboidratiG());
        dto.setDurataSuggeritaMesi(durataSuggeritaMesi);
        dto.setGiorniTrascorsi(giorniTrascorsi);
        return dto;
    }

    public FaseSintesiDTO toSintesiDTO(Fase fase) {
        FaseSintesiDTO dto = new FaseSintesiDTO();
        dto.setId(fase.getId());
        dto.setTipo(fase.getTipo());
        dto.setDataInizio(fase.getDataInizio());
        dto.setDataFine(fase.getDataFine());
        dto.setAttiva(fase.getDataFine() == null);
        return dto;
    }
}
