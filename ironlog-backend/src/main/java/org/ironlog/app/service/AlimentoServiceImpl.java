package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.AlimentoRequestDTO;
import org.ironlog.app.dto.AlimentoResponseDTO;
import org.ironlog.app.exception.AlimentoNonTrovatoException;
import org.ironlog.app.mapper.AlimentoMapper;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.FonteAlimento;
import org.ironlog.app.repository.AlimentoRepository;
import org.ironlog.app.service.definition.AlimentoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlimentoServiceImpl implements AlimentoService {

    private final AlimentoRepository alimentoRepository;
    private final AlimentoMapper alimentoMapper;

    @Override
    public List<AlimentoResponseDTO> findAll() {
        return alimentoRepository.findAllByOrderByNomeAsc().stream().map(alimentoMapper::toResponseDTO).toList();
    }

    @Override
    public List<AlimentoResponseDTO> cerca(String nome) {
        return alimentoRepository.cerca(nome).stream().map(alimentoMapper::toResponseDTO).toList();
    }

    @Override
    public AlimentoResponseDTO findById(Long id) {
        Alimento alimento = alimentoRepository.findById(id)
                .orElseThrow(() -> new AlimentoNonTrovatoException("Alimento non trovato"));
        return alimentoMapper.toResponseDTO(alimento);
    }

    @Override
    public AlimentoResponseDTO creaPersonalizzato(AlimentoRequestDTO dto) {
        Alimento alimento = alimentoMapper.toEntity(dto, FonteAlimento.PERSONALIZZATO);
        return alimentoMapper.toResponseDTO(alimentoRepository.save(alimento));
    }
}
