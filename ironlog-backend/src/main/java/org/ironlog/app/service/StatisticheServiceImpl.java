package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.DashboardDTO;
import org.ironlog.app.dto.PuntoVolumeDTO;
import org.ironlog.app.dto.VolumeGruppoDTO;
import org.ironlog.app.model.PeriodoStatistica;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Sessione;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SerieEseguitaRepository;
import org.ironlog.app.repository.SessioneRepository;
import org.ironlog.app.service.definition.StatisticheService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class StatisticheServiceImpl implements StatisticheService {

    private final SerieEseguitaRepository serieEseguitaRepository;
    private final SessioneRepository sessioneRepository;

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

    @Override
    public DashboardDTO dashboard(Utente atleta) {

        LocalDateTime ora = LocalDateTime.now();
        LocalDateTime inizioSettimana = ora.minusDays(7);

        List<Sessione> sessioni = sessioneRepository
                .findByAtletaAndEseguitaIlBetweenOrderByEseguitaIlDesc(atleta, inizioSettimana, ora);

        List<SerieEseguita> serie = serieEseguitaRepository
                .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, inizioSettimana, ora);

        DashboardDTO dto = new DashboardDTO();
        dto.setAllenamentiSettimana(sessioni.size());
        dto.setSerieSettimana(serie.size());
        dto.setVolumeSettimana(calcolaVolume(serie));

        LocalDateTime inizioSettimanaPrecedente = inizioSettimana.minusDays(7);

        List<SerieEseguita> seriePrecedenti = serieEseguitaRepository
                .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, inizioSettimanaPrecedente, inizioSettimana);

        BigDecimal volumePrecedente = calcolaVolume(seriePrecedenti);

        if (volumePrecedente.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal variazione = dto.getVolumeSettimana()
                    .subtract(volumePrecedente)
                    .divide(volumePrecedente, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, RoundingMode.HALF_UP);
            dto.setVariazioneVolumePercentuale(variazione);
        }

        dto.setStreak(calcolaStreak(atleta));

        return dto;
    }

    @Override
    public List<PuntoVolumeDTO> andamentoVolume(Utente atleta, PeriodoStatistica periodo) {

        LocalDateTime a = LocalDateTime.now();
        LocalDateTime da = periodo == PeriodoStatistica.SETTIMANA
                ? a.minusDays(7)
                : a.minusMonths(1);

        List<SerieEseguita> serie = serieEseguitaRepository
                .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, da, a);

        Map<LocalDate, BigDecimal> perGiorno = new TreeMap<>();

        for (SerieEseguita s : serie) {
            LocalDate giorno = s.getSessione().getEseguitaIl().toLocalDate();
            BigDecimal volumeSerie = s.getPeso().multiply(BigDecimal.valueOf(s.getRipetizioni()));
            perGiorno.merge(giorno, volumeSerie, BigDecimal::add);
        }

        List<PuntoVolumeDTO> punti = new ArrayList<>();
        for (Map.Entry<LocalDate, BigDecimal> e : perGiorno.entrySet()) {
            PuntoVolumeDTO p = new PuntoVolumeDTO();
            p.setData(e.getKey());
            p.setVolume(e.getValue().setScale(2, RoundingMode.HALF_UP));
            punti.add(p);
        }
        return punti;
    }

    private BigDecimal calcolaVolume(List<SerieEseguita> serie) {
        return serie.stream()
                .map(s -> s.getPeso().multiply(BigDecimal.valueOf(s.getRipetizioni())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int calcolaStreak(Utente atleta) {
        List<Sessione> sessioni = sessioneRepository.findByAtletaOrderByEseguitaIlDesc(atleta);

        Set<LocalDate> giorni = sessioni.stream()
                .map(s -> s.getEseguitaIl().toLocalDate())
                .collect(Collectors.toSet());

        LocalDate giorno = LocalDate.now();

        if (!giorni.contains(giorno)) {
            giorno = giorno.minusDays(1);
            if (!giorni.contains(giorno)) {
                return 0;
            }
        }

        int streak = 0;
        while (giorni.contains(giorno)) {
            streak++;
            giorno = giorno.minusDays(1);
        }
        return streak;
    }
}
