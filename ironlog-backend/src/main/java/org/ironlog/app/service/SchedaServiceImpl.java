package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.*;
import org.ironlog.app.exception.EsercizioNonTrovatoException;
import org.ironlog.app.exception.SchedaNonTrovataException;
import org.ironlog.app.mapper.EsercizioSchedaMapper;
import org.ironlog.app.mapper.GiornoSchedaMapper;
import org.ironlog.app.mapper.SchedaMapper;
import org.ironlog.app.model.*;
import org.ironlog.app.repository.EsercizioRepository;
import org.ironlog.app.repository.SchedaRepository;
import org.ironlog.app.service.definition.SchedaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class SchedaServiceImpl implements SchedaService {

    private final EsercizioRepository esercizioRepository;
    private final SchedaRepository schedaRepository;
    private final EsercizioSchedaMapper esercizioSchedaMapper;
    private final SchedaMapper schedaMapper;
    private final GiornoSchedaMapper giornoSchedaMapper;

    @Override
    public SchedaResponseDTO create(SchedaRequestDTO schedaRequestDTO, Utente atleta, Utente autore) {

        List<Scheda> schedeAttive = schedaRepository.findByAtletaAndAttivaTrue(atleta);
        for (Scheda s : schedeAttive) {
            s.setAttiva(false);
            schedaRepository.save(s);
        }

        Scheda scheda = schedaMapper.toEntity(schedaRequestDTO, atleta, autore);
        scheda.setAttiva(true);

        List<GiornoScheda> giorni = new ArrayList<>();

        for (int i = 0; i < schedaRequestDTO.getGiorni().size(); i++) {

            GiornoSchedaRequestDTO giornoDto = schedaRequestDTO.getGiorni().get(i);
            GiornoScheda giorno = giornoSchedaMapper.toEntity(giornoDto, i + 1, scheda);

            List<EsercizioScheda> esercizi = new ArrayList<>();

            for (int j = 0; j < giornoDto.getEsercizi().size(); j++) {

                EsercizioSchedaRequestDTO esercizioDto = giornoDto.getEsercizi().get(j);

                Esercizio esercizio = esercizioRepository.findById(esercizioDto.getEsercizioId())
                        .orElseThrow(() -> new EsercizioNonTrovatoException("Esercizio non trovato"));

                esercizi.add(esercizioSchedaMapper.toEntity(esercizioDto, esercizio, j + 1, giorno));
            }

            giorno.setEsercizi(esercizi);
            giorni.add(giorno);
        }

        scheda.setGiorni(giorni);

        return schedaMapper.toResponseDTO(schedaRepository.save(scheda));
    }

    @Override
    public List<SchedaSintesiDTO> findByAtleta(Utente atleta) {
        return schedaRepository.findByAtleta(atleta).stream().map(schedaMapper::toSintesiDTO).toList();
    }

    @Override
    public SchedaResponseDTO findByIdAndAtleta(Long id, Utente atleta) {
        Scheda scheda = schedaRepository.findByIdAndAtleta(id, atleta).orElseThrow(() -> new SchedaNonTrovataException("Scheda non trovata."));
        return schedaMapper.toResponseDTO(scheda);
    }
}
