package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.mapper.SchedaMapper;
import org.ironlog.app.mapper.UtenteMapper;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SchedaRepository;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.CoachService;

import java.util.List;

import org.ironlog.app.service.definition.SchedaService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CoachServiceImpl implements CoachService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final SchedaRepository schedaRepository;
    private final SchedaMapper schedaMapper;
    private final SchedaService schedaService;

    @Override
    public List<UtenteResponseDTO> findAtleti(Utente coach) {
        return utenteRepository.findByCoach(coach).stream().map(utenteMapper::toResponseDTO).toList();
    }

    @Override
    public SchedaResponseDTO createSchedaPerAtleta(SchedaRequestDTO dto, Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if(!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return schedaService.create(dto, atleta, coach);
    }

    @Override
    public List<SchedaSintesiDTO> findSchedeAtleta(Long atletaId, Utente coach) {
        Utente atleta = utenteRepository.findById(atletaId).orElseThrow(() -> new UtenteNonTrovatoException("Atleta non trovato"));

        if(!isCoach(coach, atleta)) {
            throw new UtenteNonTrovatoException("Atleta non trovato");
        }

        return schedaService.findByAtleta(atleta);
    }

    private boolean isCoach(Utente coach, Utente atleta) {
        return atleta.getCoach() != null && atleta.getCoach().getId().equals(coach.getId());

    }
}
