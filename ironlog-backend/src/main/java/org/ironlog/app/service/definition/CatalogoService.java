package org.ironlog.app.service.definition;

import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface CatalogoService {

    List<GruppoMuscolareResponseDTO> findAllGruppi();
    List<EsercizioResponseDTO> findAllEsercizi();
    List<EsercizioResponseDTO> findAllEserciziByGruppo(Long gruppoMuscolareId);
    List<EsercizioResponseDTO> cerca(String nome, Long gruppoId);
    void aggiungiPreferito(Long esercizioId, Utente utente);
    void rimuoviPreferito(Long esercizioId, Utente utente);
    List<EsercizioResponseDTO> findPreferiti(Utente utente);
}
