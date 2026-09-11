package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.StatisticaClienteDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.mapper.SchedaMapper;
import org.ironlog.app.mapper.UtenteMapper;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SchedaRepository;
import org.ironlog.app.repository.SerieEseguitaRepository;
import org.ironlog.app.repository.SessioneRepository;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.AssunzioneIntegratoreService;
import org.ironlog.app.service.definition.CoachService;
import org.ironlog.app.service.definition.ConsumoAcquaService;
import org.ironlog.app.service.definition.FaseService;
import org.ironlog.app.service.definition.MisurazioneCorporeaService;
import org.ironlog.app.service.definition.PianoAlimentareService;
import org.ironlog.app.service.definition.ProfiloMetabolicoService;
import org.ironlog.app.service.definition.SchedaService;
import org.ironlog.app.service.definition.VoceDiarioService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final MisurazioneCorporeaService misurazioneCorporeaService;
    private final ProfiloMetabolicoService profiloMetabolicoService;
    private final FaseService faseService;
    private final VoceDiarioService voceDiarioService;
    private final ConsumoAcquaService consumoAcquaService;
    private final AssunzioneIntegratoreService assunzioneIntegratoreService;
    private final PianoAlimentareService pianoAlimentareService;

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

    @Override
    public MisurazioneCorporeaResponseDTO createMisurazionePerAtleta(MisurazioneCorporeaRequestDTO dto, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return misurazioneCorporeaService.registra(dto, atleta);
    }

    @Override
    public List<MisurazioneCorporeaResponseDTO> findMisurazioniAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return misurazioneCorporeaService.findByAtleta(atleta);
    }

    @Override
    public ProfiloMetabolicoResponseDTO calcolaProfiloMetabolicoPerAtleta(ProfiloMetabolicoRequestDTO dto, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return profiloMetabolicoService.calcola(dto, atleta);
    }

    @Override
    public ProfiloMetabolicoResponseDTO findProfiloMetabolicoAttualeAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return profiloMetabolicoService.findAttuale(atleta);
    }

    @Override
    public FaseResponseDTO createFasePerAtleta(FaseRequestDTO dto, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return faseService.create(dto, atleta);
    }

    @Override
    public FaseResponseDTO findFaseAttivaAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return faseService.findAttiva(atleta);
    }

    @Override
    public List<FaseSintesiDTO> findFasiAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return faseService.findByAtleta(atleta);
    }

    @Override
    public FaseResponseDTO chiudiFasePerAtleta(Long faseId, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return faseService.chiudi(faseId, atleta);
    }

    @Override
    public List<VoceDiarioResponseDTO> findDiarioAtleta(Long atletaId, LocalDate data, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return voceDiarioService.findByAtletaEData(atleta, data);
    }

    @Override
    public RiepilogoDiarioDTO riepilogoDiarioAtleta(Long atletaId, LocalDate data, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return voceDiarioService.riepilogo(atleta, data);
    }

    @Override
    public List<ConsumoAcquaResponseDTO> findAcquaAtleta(Long atletaId, LocalDate data, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return consumoAcquaService.findByAtletaEData(atleta, data);
    }

    @Override
    public List<AssunzioneIntegratoreResponseDTO> findAssunzioniIntegratoriAtleta(Long atletaId, LocalDate data, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return assunzioneIntegratoreService.findByAtletaEData(atleta, data);
    }

    @Override
    public PianoAlimentareResponseDTO generaPianoAlimentarePerAtleta(Long faseId, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return pianoAlimentareService.genera(faseId, atleta);
    }

    @Override
    public List<PianoAlimentareSintesiDTO> findPianiAlimentariAtleta(Long faseId, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if (!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return pianoAlimentareService.findByFase(faseId, atleta);
    }

    private boolean isCoach(Utente coach, Utente atleta) {
        return atleta.getCoach() != null && atleta.getCoach().getId().equals(coach.getId());
    }
}