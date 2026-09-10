package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.LoginRequestDTO;
import org.ironlog.app.dto.RegisterRequestDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.exception.CredenzialiNonValideException;
import org.ironlog.app.exception.EmailGiaRegistrataException;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.mapper.UtenteMapper;
import org.ironlog.app.model.Ruolo;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.security.JwtService;
import org.ironlog.app.service.definition.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void register(RegisterRequestDTO dto) {

        if(utenteRepository.existsByEmail(dto.getEmail())) {
            throw new EmailGiaRegistrataException("Email già registrata.");
        }

        Utente utente = utenteMapper.toEntity(dto);
        utente.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Un atleta puo' scegliere in registrazione il coach che lo seguira'.
        if (dto.getRuolo() == Ruolo.ATHLETE && dto.getCoachId() != null) {
            Utente coach = utenteRepository.findById(dto.getCoachId())
                    .filter(c -> c.getRuolo() == Ruolo.COACH)
                    .orElseThrow(() -> new UtenteNonTrovatoException("Coach non trovato."));
            utente.setCoach(coach);
        }

        utenteRepository.save(utente);
    }

    @Override
    public List<UtenteResponseDTO> findCoachDisponibili() {
        return utenteRepository.findByRuoloOrderByCognomeAsc(Ruolo.COACH).stream()
                .map(utenteMapper::toResponseDTO)
                .toList();
    }

    @Override
    public String login(LoginRequestDTO dto) {

        Utente utente = utenteRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new CredenzialiNonValideException("Credenziali non valide."));

        if (!passwordEncoder.matches(dto.getPassword(), utente.getPassword())) throw new CredenzialiNonValideException("Credenziali non valide.");

        return jwtService.createToken(utente);
    }
}
