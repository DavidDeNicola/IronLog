package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.CambioPasswordRequestDTO;
import org.ironlog.app.exception.PasswordAttualeErrataException;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.ProfiloService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfiloServiceImpl implements ProfiloService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void cambiaPassword(Utente utente, CambioPasswordRequestDTO request) {
        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        if (!passwordEncoder.matches(request.getPasswordAttuale(), gestito.getPassword())) {
            throw new PasswordAttualeErrataException("La password attuale non è corretta");
        }

        gestito.setPassword(passwordEncoder.encode(request.getNuovaPassword()));
        utenteRepository.save(gestito);
    }
}