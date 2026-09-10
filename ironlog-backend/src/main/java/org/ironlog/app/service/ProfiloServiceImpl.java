package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.CambioPasswordRequestDTO;
import org.ironlog.app.exception.PasswordAttualeErrataException;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.SchedaRepository;
import org.ironlog.app.repository.SessioneRepository;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.ProfiloService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfiloServiceImpl implements ProfiloService {

    private final UtenteRepository utenteRepository;
    private final SessioneRepository sessioneRepository;
    private final SchedaRepository schedaRepository;
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

    @Override
    @Transactional
    public void eliminaProfilo(Utente utente) {
        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        // 1. Sessioni (con le serie a cascata). PRIMA delle schede: le sessioni
        //    referenziano i giorni delle schede (giorno_id), quindi vanno tolte
        //    prima di eliminare le schede a cui quei giorni appartengono.
        sessioneRepository.deleteAll(sessioneRepository.findByAtleta(gestito));

        // 2. Schede dell'atleta (con giorni ed esercizi-scheda a cascata).
        schedaRepository.deleteAll(schedaRepository.findByAtleta(gestito));

        // 3. Sgancia eventuali atleti che avevano questo utente come coach
        //    (coach_id è nullable): non li elimino, resto solo senza coach.
        List<Utente> atletiSeguiti = utenteRepository.findByCoach(gestito);
        atletiSeguiti.forEach(atleta -> atleta.setCoach(null));
        utenteRepository.saveAll(atletiSeguiti);

        // 4. Elimina l'utente: le righe di utente_preferito vengono rimosse in
        //    automatico (è il lato proprietario della ManyToMany), e la sua
        //    stessa relazione coach sparisce con la riga.
        utenteRepository.delete(gestito);
    }
}