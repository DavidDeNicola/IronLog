package org.ironlog.app.service.definition;

import org.ironlog.app.dto.IntegratoreRequestDTO;
import org.ironlog.app.dto.IntegratoreResponseDTO;

import java.util.List;

public interface IntegratoreService {

    List<IntegratoreResponseDTO> findAll();

    IntegratoreResponseDTO create(IntegratoreRequestDTO dto);
}
