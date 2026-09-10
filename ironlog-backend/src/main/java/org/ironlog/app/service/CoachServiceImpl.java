package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.StatisticaClienteDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.mapper.SchedaMapper;
import org.ironlog.app.mapper.UtenteMapper;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SchedaRepository;
import org.ironlog.app.repository.SerieEseguitaRepository;
import org.ironlog.app.repository.SessioneRepository;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.CoachService;
import org.ironlog.app.service.definition.SchedaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final SchedaRepository schedaRepository;
    private final SchedaMapper schedaMapper;
    private final SchedaService schedaService;
    private final SessioneRepository sessioneRepository;
    private final SerieEseguitaRepository serieEseguitaRepository;

    @Override
    public List<UtenteResponseDTO> findAtleti(Utente coach) {
        return utenteRepository.findByCoach(coach).stream().map(utenteMapper::toResponseDTO).toList();
    }

    @Override
    public SchedaResponseDTO createSchedaPerAtleta(SchedaRequestDTO dto, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return schedaService.create(dto, atleta, coach);
    }

    @Override
    public List<SchedaSintesiDTO> findSchedeAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return schedaService.findByAtleta(atleta);
    }

    @Override
    public List<StatisticaClienteDTO> statisticheClienti(Utente coach) {
        LocalDateTime ora = LocalDateTime.now();
        LocalDateTime trentaGiorniFa = ora.minusDays(30);
        LocalDateTime sessantaGiorniFa = ora.minusDays(60);

        return utenteRepository.findByCoach(coach).stream().map(atleta -> {
            StatisticaClienteDTO dto = new StatisticaClienteDTO();
            dto.setAtletaId(atleta.getId());
            dto.setNome(atleta.getNome());
            dto.setCognome(atleta.getCognome());
            dto.setNumeroSchede(schedaRepository.findByAtleta(atleta).size());
            dto.setAllenamentiUltimi30Giorni(
                    sessioneRepository.findByAtletaAndEseguitaIlBetweenOrderByEseguitaIlDesc(atleta, trentaGiorniFa, ora).size());

            List<SerieEseguita> recenti = serieEseguitaRepository
                    .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, trentaGiorniFa, ora);
            List<SerieEseguita> precedenti = serieEseguitaRepository
                    .findBySessioneAtletaAndSessioneEseguitaIlBetween(atleta, sessantaGiorniFa, trentaGiorniFa);
            dto.setProgressione(calcolaProgressione(recenti, precedenti));

            return dto;
        }).toList();
    }

    private String calcolaProgressione(List<SerieEseguita> recenti, List<SerieEseguita> precedenti) {
        Map<Long, BigDecimal> miglioreRecente = miglioreMassimalePerEsercizio(recenti);
        Map<Long, BigDecimal> migliorePrecedente = miglioreMassimalePerEsercizio(precedenti);

        int migliorati = 0;
        int peggiorati = 0;
        int confrontati = 0;

        for (Map.Entry<Long, BigDecimal> voce : miglioreRecente.entrySet()) {
            BigDecimal prima = migliorePrecedente.get(voce.getKey());
            if (prima == null) {
                continue;
            }
            confrontati++;
            int cmp = voce.getValue().compareTo(prima);
            if (cmp > 0) {
                migliorati++;
            } else if (cmp < 0) {
                peggiorati++;
            }
        }

        if (confrontati == 0) {
            return "DATI_INSUFFICIENTI";
        }
        if (migliorati > peggiorati) {
            return "IN_PROGRESSO";
        }
        if (peggiorati > migliorati) {
            return "IN_CALO";
        }
        return "STABILE";
    }

    private Map<Long, BigDecimal> miglioreMassimalePerEsercizio(List<SerieEseguita> serie) {
        Map<Long, BigDecimal> massimi = new HashMap<>();
        for (SerieEseguita s : serie) {
            Long esercizioId = s.getEsercizio().getId();
            massimi.merge(esercizioId, s.getMassimaleStimato(), (a, b) -> a.compareTo(b) >= 0 ? a : b);
        }
        return massimi;
    }

    private boolean isCoach(Utente coach, Utente atleta) {
        return atleta.getCoach() != null && atleta.getCoach().getId().equals(coach.getId());
    }
}