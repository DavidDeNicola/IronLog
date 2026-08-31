package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.mapper.EsercizioMapper;
import org.ironlog.app.mapper.GruppoMuscolareMapper;
import org.ironlog.app.model.Esercizio;
import org.ironlog.app.model.GruppoMuscolare;
import org.ironlog.app.repository.EsercizioRepository;
import org.ironlog.app.repository.GruppoMuscolareRepository;
import org.ironlog.app.service.definition.CatalogoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CatalogoServiceImpl implements CatalogoService {

    private final GruppoMuscolareRepository gruppoMuscolareRepository;
    private final EsercizioRepository esercizioRepository;
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
}
