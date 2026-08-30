package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.LoginRequestDTO;
import org.ironlog.app.dto.RegisterRequestDTO;
import org.ironlog.app.exception.CredenzialiNonValideException;
import org.ironlog.app.exception.EmailGiaRegistrataException;
import org.ironlog.app.mapper.UtenteMapper;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.security.JwtService;
import org.ironlog.app.service.definition.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequestDTO dto) {

        if(utenteRepository.existsByEmail(dto.getEmail())) {
            throw new EmailGiaRegistrataException("Email già registrata.");
        }

        Utente utente = utenteMapper.toEntity(dto);
        utente.setPassword(passwordEncoder.encode(dto.getPassword()));
        utenteRepository.save(utente);
    }

    @Override
    public String login(LoginRequestDTO dto) {

        Utente utente = utenteRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new CredenzialiNonValideException("Credenziali non valide."));

        if (!passwordEncoder.matches(dto.getPassword(), utente.getPassword())) throw new CredenzialiNonValideException("Credenziali non valide.");

        return jwtService.createToken(utente);
    }
}
