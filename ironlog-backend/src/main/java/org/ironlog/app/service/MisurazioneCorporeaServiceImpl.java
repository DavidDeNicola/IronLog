package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.exception.MisurazioneNonTrovataException;
import org.ironlog.app.mapper.MisurazioneCorporeaMapper;
import org.ironlog.app.model.MisurazioneCorporea;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.MisurazioneCorporeaRepository;
import org.ironlog.app.service.definition.MisurazioneCorporeaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MisurazioneCorporeaServiceImpl implements MisurazioneCorporeaService {

    private final MisurazioneCorporeaRepository misurazioneCorporeaRepository;
    private final MisurazioneCorporeaMapper misurazioneCorporeaMapper;

    @Override
    public MisurazioneCorporeaResponseDTO registra(MisurazioneCorporeaRequestDTO dto, Utente atleta) {
        MisurazioneCorporea misurazione = misurazioneCorporeaMapper.toEntity(dto, atleta);
        return misurazioneCorporeaMapper.toResponseDTO(misurazioneCorporeaRepository.save(misurazione));
    }

    @Override
    public List<MisurazioneCorporeaResponseDTO> findByAtleta(Utente atleta) {
        return misurazioneCorporeaRepository.findByAtletaOrderByDataDesc(atleta).stream()
                .map(misurazioneCorporeaMapper::toResponseDTO)
                .toList();
    }

    @Override
    public MisurazioneCorporeaResponseDTO findUltima(Utente atleta) {
        MisurazioneCorporea misurazione = misurazioneCorporeaRepository.findFirstByAtletaOrderByDataDesc(atleta)
                .orElseThrow(() -> new MisurazioneNonTrovataException("Nessuna misurazione corporea trovata"));
        return misurazioneCorporeaMapper.toResponseDTO(misurazione);
    }
}
