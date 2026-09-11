package org.ironlog.app.service.definition;

import org.ironlog.app.dto.AlimentoRequestDTO;
import org.ironlog.app.dto.AlimentoResponseDTO;

import java.util.List;

public interface AlimentoService {

    List<AlimentoResponseDTO> findAll();

    List<AlimentoResponseDTO> cerca(String nome);

    AlimentoResponseDTO findById(Long id);

    AlimentoResponseDTO creaPersonalizzato(AlimentoRequestDTO dto);
}
