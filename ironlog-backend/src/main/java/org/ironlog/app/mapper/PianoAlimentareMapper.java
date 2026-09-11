package org.ironlog.app.mapper;

import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.dto.VocePianoAlimentareResponseDTO;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.PianoAlimentare;
import org.ironlog.app.model.VocePianoAlimentare;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PianoAlimentareMapper {

    private static final BigDecimal CENTO = BigDecimal.valueOf(100);

    public PianoAlimentareResponseDTO toResponseDTO(PianoAlimentare piano) {
        PianoAlimentareResponseDTO dto = new PianoAlimentareResponseDTO();
        dto.setId(piano.getId());
        dto.setFaseId(piano.getFase().getId());
        dto.setDataGenerazione(piano.getDataGenerazione());
        dto.setVincoliSoddisfatti(piano.getVincoliSoddisfatti());

        BigDecimal calorieTotali = BigDecimal.ZERO;
        BigDecimal proteineTotali = BigDecimal.ZERO;
        BigDecimal grassiTotali = BigDecimal.ZERO;
        BigDecimal carboidratiTotali = BigDecimal.ZERO;

        List<VocePianoAlimentareResponseDTO> voci = new ArrayList<>();
        for (VocePianoAlimentare voce : piano.getVoci()) {
            VocePianoAlimentareResponseDTO voceDTO = toVoceResponseDTO(voce);
            voci.add(voceDTO);
            calorieTotali = calorieTotali.add(voceDTO.getCalorie());
            proteineTotali = proteineTotali.add(voceDTO.getProteineG());
            grassiTotali = grassiTotali.add(voceDTO.getGrassiG());
            carboidratiTotali = carboidratiTotali.add(voceDTO.getCarboidratiG());
        }

        dto.setVoci(voci);
        dto.setCalorieTotali(calorieTotali);
        dto.setProteineTotali(proteineTotali);
        dto.setGrassiTotali(grassiTotali);
        dto.setCarboidratiTotali(carboidratiTotali);
        return dto;
    }

    public PianoAlimentareSintesiDTO toSintesiDTO(PianoAlimentare piano) {
        PianoAlimentareSintesiDTO dto = new PianoAlimentareSintesiDTO();
        dto.setId(piano.getId());
        dto.setDataGenerazione(piano.getDataGenerazione());
        dto.setVincoliSoddisfatti(piano.getVincoliSoddisfatti());
        return dto;
    }

    private VocePianoAlimentareResponseDTO toVoceResponseDTO(VocePianoAlimentare voce) {
        Alimento alimento = voce.getAlimento();
        BigDecimal fattore = voce.getQuantitaGrammi().divide(CENTO, 6, RoundingMode.HALF_UP);

        VocePianoAlimentareResponseDTO dto = new VocePianoAlimentareResponseDTO();
        dto.setAlimentoId(alimento.getId());
        dto.setAlimentoNome(alimento.getNome());
        dto.setTipoPasto(voce.getTipoPasto());
        dto.setQuantitaGrammi(voce.getQuantitaGrammi());
        dto.setCalorie(alimento.getCalorie100g().multiply(fattore).setScale(2, RoundingMode.HALF_UP));
        dto.setProteineG(alimento.getProteineG().multiply(fattore).setScale(2, RoundingMode.HALF_UP));
        dto.setGrassiG(alimento.getGrassiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP));
        dto.setCarboidratiG(alimento.getCarboidratiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP));
        return dto;
    }
}
