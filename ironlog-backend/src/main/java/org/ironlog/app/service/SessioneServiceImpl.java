package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.*;
import org.ironlog.app.exception.*;
import org.ironlog.app.mapper.SerieEseguitaMapper;
import org.ironlog.app.mapper.SessioneMapper;
import org.ironlog.app.model.*;
import org.ironlog.app.repository.*;
import org.ironlog.app.service.definition.SessioneService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class SessioneServiceImpl implements SessioneService {

    private final SessioneRepository sessioneRepository;
    private final SerieEseguitaRepository serieEseguitaRepository;
    private final GiornoSchedaRepository giornoSchedaRepository;
    private final EsercizioSchedaRepository esercizioSchedaRepository;
    EsercizioSchedaRepository serieSchedaRepository;
    private final EsercizioRepository esercizioRepository;
    private final SessioneMapper sessioneMapper;
    private final SerieEseguitaMapper serieEseguitaMapper;


    @Override
    public SessioneResponseDTO apri(SessioneRequestDTO dto, Utente atleta) {
        if (!sessioneRepository.findByAtletaAndConclusaIlNull(atleta).isEmpty()) {
            throw new SessioneGiaApertaException("Sessione gia aperta");
        }
        Sessione sessione = new Sessione();
        sessione.setAtleta(atleta);
        sessione.setEseguitaIl(LocalDateTime.now());
        sessione.setNote(dto.getNote());
        if (dto.getGiornoSchedaId() != null) {
            sessione.setGiornoScheda(giornoSchedaRepository.findByIdAndSchedaAtleta(dto.getGiornoSchedaId(), atleta).orElseThrow(() -> new GiornoNonTrovatoException("Giorno non trovato")));
        }
        return sessioneMapper.toResponseDTO(sessioneRepository.save(sessione));
    }

    @Override
    public SerieEseguitaResponseDTO registraSerie(Long sessioneId, SerieEseguitaRequestDTO dto, Utente atleta) {

        Sessione sessione = sessioneRepository.findByIdAndAtleta(sessioneId, atleta).orElseThrow(() -> new SessioneNonTrovataException("Sessione non trovata"));

        if(sessione.getConclusaIl() != null){
            throw new SessioneGiaConclusaException("Sessione gia conclusa");
        }

        Esercizio esercizio = esercizioRepository.findById(dto.getEsercizioId()).orElseThrow(() -> new EsercizioNonTrovatoException("Esercizio non trovato"));

        Integer ripObiettivo = null;

        if(dto.getEsercizioSchedaId() != null){
            EsercizioScheda esercizioScheda = esercizioSchedaRepository.findByIdAndGiornoSchedaSchedaAtleta(dto.getEsercizioSchedaId(), atleta).orElseThrow(() -> new EsercizioNonTrovatoException("Esercizio non trovato"));
            ripObiettivo = esercizioScheda.getRipetizioni();
        }

        int numeroSerie = sessione.getSerie().size() + 1;

        BigDecimal fattore = BigDecimal.ONE.add(
                BigDecimal.valueOf(dto.getRipetizioni())
                        .divide(BigDecimal.valueOf(30), 4, RoundingMode.HALF_UP)
        );

        BigDecimal massimaleStimato = dto.getPeso()
                .multiply(fattore)
                .setScale(2, RoundingMode.HALF_UP);

        SerieEseguita serie = serieEseguitaMapper.toEntity(dto, sessione, esercizio, numeroSerie, ripObiettivo, massimaleStimato);
        return serieEseguitaMapper.toResponseDTO(serieEseguitaRepository.save(serie));
    }

    @Override
    public RiepilogoSessioneDTO concludi(Long sessioneId, Utente atleta) {
        Sessione sessione = sessioneRepository.findByIdAndAtleta(sessioneId, atleta).orElseThrow(() -> new SessioneNonTrovataException("Sessione non trovata"));
        if(sessione.getConclusaIl() != null){
            throw new SessioneGiaConclusaException("Sessione gia conclusa");
        }
        sessione.setConclusaIl(LocalDateTime.now());
        sessioneRepository.save(sessione);
        RiepilogoSessioneDTO dto = new RiepilogoSessioneDTO();
        dto.setSessioneId(sessioneId);
        dto.setEseguitaIl(sessione.getEseguitaIl());
        dto.setConclusaIl(sessione.getConclusaIl());
        dto.setDurataMinuti(Duration.between(sessione.getEseguitaIl(), sessione.getConclusaIl()).toMinutes());
        dto.setNumeroSerie(sessione.getSerie().size());

        BigDecimal volumeTotale = sessione.getSerie().stream()
                .map(s -> s.getPeso().multiply(BigDecimal.valueOf(s.getRipetizioni())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        int serieCompletate = (int) sessione.getSerie().stream()
                .filter(s -> s.getRipObiettivo() != null && s.getRipetizioni() >= s.getRipObiettivo())
                .count();

        dto.setVolumeTotale(volumeTotale);
        dto.setSerieCompletate(serieCompletate);
        return dto;
    }

    @Override
    public SessioneResponseDTO findAperta(Utente atleta) {
        return sessioneRepository.findByAtletaAndConclusaIlNull(atleta).stream().findFirst().map(sessioneMapper::toResponseDTO).orElse(null);
    }

    @Override
    public List<SessioneSintesiDTO> findStorico(Utente atleta) {
        return sessioneRepository.findByAtletaOrderByEseguitaIlDesc(atleta)
                .stream()
                .map(sessioneMapper::toSintesiDTO)
                .toList();
    }
}
