package org.ironlog.app.service.definition;

import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.VoceDiarioRequestDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.model.Utente;

import java.time.LocalDate;
import java.util.List;

public interface VoceDiarioService {

    VoceDiarioResponseDTO registra(VoceDiarioRequestDTO dto, Utente atleta);

    List<VoceDiarioResponseDTO> findByAtletaEData(Utente atleta, LocalDate data);

    void elimina(Long id, Utente atleta);

    RiepilogoDiarioDTO riepilogo(Utente atleta, LocalDate data);
}
