package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.exception.EsercizioNonTrovatoException;
import org.ironlog.app.exception.UtenteNonTrovatoException;
import org.ironlog.app.mapper.EsercizioMapper;
import org.ironlog.app.mapper.GruppoMuscolareMapper;
import org.ironlog.app.model.Esercizio;
import org.ironlog.app.model.GruppoMuscolare;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.EsercizioRepository;
import org.ironlog.app.repository.GruppoMuscolareRepository;
import org.ironlog.app.repository.UtenteRepository;
import org.ironlog.app.service.definition.CatalogoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CatalogoServiceImpl implements CatalogoService {

    private final GruppoMuscolareRepository gruppoMuscolareRepository;
    private final EsercizioRepository esercizioRepository;
    private final UtenteRepository utenteRepository;
    private final GruppoMuscolareMapper gruppoMuscolareMapper;
    private final EsercizioMapper esercizioMapper;

    @Override
    public List<GruppoMuscolareResponseDTO> findAllGruppi() {
        List<GruppoMuscolare> gruppi = gruppoMuscolareRepository.findAllByOrderByNomeAsc();
        return gruppi.stream().map(g -> gruppoMuscolareMapper.toResponseDTO(g)).toList();
    }

    @Override
    public List<EsercizioResponseDTO> findAllEsercizi() {
        List<Esercizio> esercizi = esercizioRepository.findAllByOrderByNomeAsc();
        return esercizi.stream().map(e -> esercizioMapper.toResponseDTO(e)).toList();
    }

    @Override
    public List<EsercizioResponseDTO> findAllEserciziByGruppo(Long gruppoMuscolareId) {
        List<Esercizio> esercizi = esercizioRepository.findByGruppoMuscolareIdOrderByNomeAsc(gruppoMuscolareId);
        return esercizi.stream().map(e -> esercizioMapper.toResponseDTO(e)).toList();
    }

    @Override
    public List<EsercizioResponseDTO> cerca(String nome, Long gruppoId) {
        return esercizioRepository.cerca(nome, gruppoId).stream().map(esercizioMapper::toResponseDTO).toList();
    }

    @Override
    public void aggiungiPreferito(Long esercizioId, Utente utente) {
        Esercizio esercizio = esercizioRepository.findById(esercizioId)
                .orElseThrow(() -> new EsercizioNonTrovatoException("Esercizio non trovato"));

        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        if (!gestito.getPreferiti().contains(esercizio)) {
            gestito.getPreferiti().add(esercizio);
            utenteRepository.save(gestito);
        }
    }

    @Override
    public void rimuoviPreferito(Long esercizioId, Utente utente) {
        Esercizio esercizio = esercizioRepository.findById(esercizioId)
                .orElseThrow(() -> new EsercizioNonTrovatoException("Esercizio non trovato"));

        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        gestito.getPreferiti().remove(esercizio);
        utenteRepository.save(gestito);
    }

    @Override
    public List<EsercizioResponseDTO> findPreferiti(Utente utente) {
        Utente gestito = utenteRepository.findById(utente.getId())
                .orElseThrow(() -> new UtenteNonTrovatoException("Utente non trovato"));

        return gestito.getPreferiti().stream()
                .map(esercizioMapper::toResponseDTO)
                .toList();
    }
}
