package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.IntegratoreRequestDTO;
import org.ironlog.app.dto.IntegratoreResponseDTO;
import org.ironlog.app.mapper.IntegratoreMapper;
import org.ironlog.app.model.Integratore;
import org.ironlog.app.repository.IntegratoreRepository;
import org.ironlog.app.service.definition.IntegratoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntegratoreServiceImpl implements IntegratoreService {

    private final IntegratoreRepository integratoreRepository;
    private final IntegratoreMapper integratoreMapper;

    @Override
    public List<IntegratoreResponseDTO> findAll() {
        return integratoreRepository.findAllByOrderByNomeAsc().stream().map(integratoreMapper::toResponseDTO).toList();
    }

    @Override
    public IntegratoreResponseDTO create(IntegratoreRequestDTO dto) {
        Integratore integratore = integratoreMapper.toEntity(dto);
        return integratoreMapper.toResponseDTO(integratoreRepository.save(integratore));
    }
}
