package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.VolumeGruppoDTO;
import org.ironlog.app.model.PeriodoStatistica;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SerieEseguitaRepository;
import org.ironlog.app.service.definition.StatisticheService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class StatisticheServiceImpl implements StatisticheService {

    private final SerieEseguitaRepository serieEseguitaRepository;

    @Override
    public List<VolumeGruppoDTO> volumePerGruppo(Utente atleta, PeriodoStatistica periodo) {

        LocalDateTime a = LocalDateTime.now();
        LocalDateTime da = periodo == PeriodoStatistica.SETTIMANA
                ? a.minusDays(7)
                : a.minusMonths(1);

        List<SerieEseguita> serie = serieEseguitaRepository
                .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, da, a);

        Map<String, VolumeGruppoDTO> perGruppo = new LinkedHashMap<>();

        for (SerieEseguita s : serie) {
            String gruppo = s.getEsercizio().getGruppoMuscolare().getNome();

            VolumeGruppoDTO dto = perGruppo.get(gruppo);
            if (dto == null) {
                dto = new VolumeGruppoDTO();
                dto.setGruppoMuscolareNome(gruppo);
                dto.setVolumeTotale(BigDecimal.ZERO);
                dto.setNumeroSerie(0);
                perGruppo.put(gruppo, dto);
            }

            BigDecimal volumeSerie = s.getPeso().multiply(BigDecimal.valueOf(s.getRipetizioni()));
            dto.setVolumeTotale(dto.getVolumeTotale().add(volumeSerie));
            dto.setNumeroSerie(dto.getNumeroSerie() + 1);
        }

        return perGruppo.values().stream().toList();
    }
}
