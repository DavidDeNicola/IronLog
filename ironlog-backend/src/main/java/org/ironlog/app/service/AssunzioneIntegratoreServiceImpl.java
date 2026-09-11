package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.AssunzioneIntegratoreRequestDTO;
import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.exception.IntegratoreNonTrovatoException;
import org.ironlog.app.mapper.AssunzioneIntegratoreMapper;
import org.ironlog.app.model.AssunzioneIntegratore;
import org.ironlog.app.model.Integratore;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.AssunzioneIntegratoreRepository;
import org.ironlog.app.repository.IntegratoreRepository;
import org.ironlog.app.service.definition.AssunzioneIntegratoreService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssunzioneIntegratoreServiceImpl implements AssunzioneIntegratoreService {

    private final AssunzioneIntegratoreRepository assunzioneIntegratoreRepository;
    private final IntegratoreRepository integratoreRepository;
    private final AssunzioneIntegratoreMapper assunzioneIntegratoreMapper;

    @Override
    public AssunzioneIntegratoreResponseDTO registra(AssunzioneIntegratoreRequestDTO dto, Utente atleta) {
        Integratore integratore = integratoreRepository.findById(dto.getIntegratoreId())
                .orElseThrow(() -> new IntegratoreNonTrovatoException("Integratore non trovato"));

        AssunzioneIntegratore assunzione = assunzioneIntegratoreMapper.toEntity(dto, atleta, integratore);
        return assunzioneIntegratoreMapper.toResponseDTO(assunzioneIntegratoreRepository.save(assunzione));
    }

    @Override
    public List<AssunzioneIntegratoreResponseDTO> findByAtletaEData(Utente atleta, LocalDate data) {
        return assunzioneIntegratoreRepository.findByAtletaAndData(atleta, data).stream()
                .map(assunzioneIntegratoreMapper::toResponseDTO)
                .toList();
    }
}
