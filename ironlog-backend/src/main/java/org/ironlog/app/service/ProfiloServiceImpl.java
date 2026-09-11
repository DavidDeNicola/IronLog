package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.CambioPasswordRequestDTO;
import org.ironlog.app.dto.DatiBiometriciRequestDTO;
import org.ironlog.app.exception.PasswordAttualeErrataException;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.model.Scheda;
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
        //    referenziano i giorni delle schede (giorno_id).
        sessioneRepository.deleteAll(sessioneRepository.findByAtleta(gestito));

        // 2. Schede dell'atleta (con giorni ed esercizi-scheda a cascata).
        schedaRepository.deleteAll(schedaRepository.findByAtleta(gestito));

        // 2b. Schede che l'utente ha scritto per altri atleti (è autore ma non
        //     atleta): restano all'atleta, l'autore diventa l'atleta stesso.
        //     Dopo la cancellazione al punto 2, qui rimangono solo quelle di altri.
        List<Scheda> scritteDaLui = schedaRepository.findByAutore(gestito);
        for (Scheda scheda : scritteDaLui) {
            scheda.setAutore(scheda.getAtleta());
        }
        schedaRepository.saveAll(scritteDaLui);

        // 3. Sgancia gli atleti che avevano questo utente come coach (coach_id nullable).
        List<Utente> atletiSeguiti = utenteRepository.findByCoach(gestito);
        atletiSeguiti.forEach(atleta -> atleta.setCoach(null));
        utenteRepository.saveAll(atletiSeguiti);

        // 4. Elimina l'utente: le righe di utente_preferito vengono rimosse in
        //    automatico (lato proprietario della ManyToMany).
        utenteRepository.delete(gestito);
    }

    @Override
    @Transactional
    public void aggiornaDatiBiometrici(Utente utente, DatiBiometriciRequestDTO request) {
        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        gestito.setSesso(request.getSesso());
        gestito.setDataNascita(request.getDataNascita());
        utenteRepository.save(gestito);
    }
}