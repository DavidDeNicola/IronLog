package org.ironlog.app.mapper;

import org.ironlog.app.dto.VoceDiarioRequestDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.Utente;
import org.ironlog.app.model.VoceDiario;
import org.ironlog.app.service.VoceDiarioCalcolata;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class VoceDiarioMapper {

    private static final BigDecimal CENTO = BigDecimal.valueOf(100);

    public VoceDiario toEntity(VoceDiarioRequestDTO dto, Utente atleta, Alimento alimento) {
        VoceDiario voce = new VoceDiario();
        voce.setAtleta(atleta);
        voce.setAlimento(alimento);
        voce.setData(dto.getData());
        voce.setTipoPasto(dto.getTipoPasto());
        voce.setQuantitaGrammi(dto.getQuantitaGrammi());
        return voce;
    }

    public VoceDiarioResponseDTO toResponseDTO(VoceDiario voce) {
        VoceDiarioCalcolata calcolata = calcola(voce);

        VoceDiarioResponseDTO dto = new VoceDiarioResponseDTO();
        dto.setId(voce.getId());
        dto.setAlimentoId(voce.getAlimento().getId());
        dto.setAlimentoNome(voce.getAlimento().getNome());
        dto.setData(voce.getData());
        dto.setTipoPasto(voce.getTipoPasto());
        dto.setQuantitaGrammi(voce.getQuantitaGrammi());
        dto.setCalorie(calcolata.calorie());
        dto.setProteineG(calcolata.proteineG());
        dto.setGrassiG(calcolata.grassiG());
        dto.setCarboidratiG(calcolata.carboidratiG());
        return dto;
    }

    public VoceDiarioCalcolata calcola(VoceDiario voce) {
        BigDecimal fattore = voce.getQuantitaGrammi().divide(CENTO, 6, RoundingMode.HALF_UP);
        Alimento alimento = voce.getAlimento();

        return new VoceDiarioCalcolata(
                alimento.getCalorie100g().multiply(fattore).setScale(2, RoundingMode.HALF_UP),
                alimento.getProteineG().multiply(fattore).setScale(2, RoundingMode.HALF_UP),
                alimento.getGrassiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP),
                alimento.getCarboidratiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
