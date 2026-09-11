package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ConsumoAcquaRequestDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.mapper.ConsumoAcquaMapper;
import org.ironlog.app.model.ConsumoAcqua;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.ConsumoAcquaRepository;
import org.ironlog.app.service.definition.ConsumoAcquaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumoAcquaServiceImpl implements ConsumoAcquaService {

    private final ConsumoAcquaRepository consumoAcquaRepository;
    private final ConsumoAcquaMapper consumoAcquaMapper;

    @Override
    public ConsumoAcquaResponseDTO registra(ConsumoAcquaRequestDTO dto, Utente atleta) {
        ConsumoAcqua consumo = consumoAcquaMapper.toEntity(dto, atleta);
        return consumoAcquaMapper.toResponseDTO(consumoAcquaRepository.save(consumo));
    }

    @Override
    public List<ConsumoAcquaResponseDTO> findByAtletaEData(Utente atleta, LocalDate data) {
        return consumoAcquaRepository.findByAtletaAndData(atleta, data).stream()
                .map(consumoAcquaMapper::toResponseDTO)
                .toList();
    }
}
