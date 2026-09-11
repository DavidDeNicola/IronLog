package org.ironlog.app.mapper;

import org.ironlog.app.dto.AlimentoRequestDTO;
import org.ironlog.app.dto.AlimentoResponseDTO;
import org.ironlog.app.dto.MicronutrienteRequestDTO;
import org.ironlog.app.dto.MicronutrienteResponseDTO;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.FonteAlimento;
import org.ironlog.app.model.MicronutrienteAlimento;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlimentoMapper {

    public Alimento toEntity(AlimentoRequestDTO dto, FonteAlimento fonte) {
        Alimento alimento = new Alimento();
        alimento.setNome(dto.getNome());
        alimento.setFonte(fonte);
        alimento.setCalorie100g(dto.getCalorie100g());
        alimento.setProteineG(dto.getProteineG());
        alimento.setGrassiG(dto.getGrassiG());
        alimento.setCarboidratiG(dto.getCarboidratiG());
        alimento.setFibreG(dto.getFibreG());
        alimento.setZuccheriG(dto.getZuccheriG());
        alimento.setSodioMg(dto.getSodioMg());

        if (dto.getMicronutrienti() != null) {
            List<MicronutrienteAlimento> micronutrienti = new ArrayList<>();
            for (MicronutrienteRequestDTO m : dto.getMicronutrienti()) {
                MicronutrienteAlimento micronutriente = new MicronutrienteAlimento();
                micronutriente.setAlimento(alimento);
                micronutriente.setNomeNutriente(m.getNomeNutriente());
                micronutriente.setQuantita(m.getQuantita());
                micronutriente.setUnita(m.getUnita());
                micronutrienti.add(micronutriente);
            }
            alimento.setMicronutrienti(micronutrienti);
        }

        return alimento;
    }

    public AlimentoResponseDTO toResponseDTO(Alimento alimento) {
        AlimentoResponseDTO dto = new AlimentoResponseDTO();
        dto.setId(alimento.getId());
        dto.setNome(alimento.getNome());
        dto.setFonte(alimento.getFonte());
        dto.setCalorie100g(alimento.getCalorie100g());
        dto.setProteineG(alimento.getProteineG());
        dto.setGrassiG(alimento.getGrassiG());
        dto.setCarboidratiG(alimento.getCarboidratiG());
        dto.setFibreG(alimento.getFibreG());
        dto.setZuccheriG(alimento.getZuccheriG());
        dto.setSodioMg(alimento.getSodioMg());
        dto.setMicronutrienti(alimento.getMicronutrienti().stream().map(this::toMicronutrienteDTO).toList());
        return dto;
    }

    private MicronutrienteResponseDTO toMicronutrienteDTO(MicronutrienteAlimento m) {
        MicronutrienteResponseDTO dto = new MicronutrienteResponseDTO();
        dto.setNomeNutriente(m.getNomeNutriente());
        dto.setQuantita(m.getQuantita());
        dto.setUnita(m.getUnita());
        return dto;
    }
}
