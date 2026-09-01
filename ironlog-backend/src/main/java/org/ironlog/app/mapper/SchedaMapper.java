package org.ironlog.app.mapper;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.model.Scheda;
import org.ironlog.app.model.Utente;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SchedaMapper {

    private final GiornoSchedaMapper giornoSchedaMapper;

    public Scheda toEntity(SchedaRequestDTO dto, Utente atleta, Utente autore) {
        Scheda scheda = new Scheda();
        scheda.setNome(dto.getNome());
        scheda.setAtleta(atleta);
        scheda.setAutore(autore);
        scheda.setNote(dto.getNote());
        scheda.setDataInizio(dto.getDataInizio());
        return scheda;
    }

    public SchedaSintesiDTO toSintesiDTO(Scheda scheda){
        SchedaSintesiDTO dto = new SchedaSintesiDTO();
        dto.setId(scheda.getId());
        dto.setNome(scheda.getNome());
        dto.setDataInizio(scheda.getDataInizio());
        dto.setAttiva(scheda.getAttiva());
        dto.setNumeroGiorni(scheda.getGiorni().size());
        return dto;
    }

    public SchedaResponseDTO toResponseDTO(Scheda scheda){
        SchedaResponseDTO dto = new SchedaResponseDTO();
        dto.setId(scheda.getId());
        dto.setNome(scheda.getNome());
        dto.setDataInizio(scheda.getDataInizio());
        dto.setAttiva(scheda.getAttiva());
        dto.setNumeroGiorni(scheda.getGiorni().size());
        dto.setNote(scheda.getNote());
        dto.setAtletaNome(scheda.getAtleta().getNome() + " " + scheda.getAtleta().getCognome());
        dto.setAutoreNome(scheda.getAutore().getNome() + " " + scheda.getAutore().getCognome());
        dto.setGiorni(scheda.getGiorni().stream().map(giornoSchedaMapper::toResponseDTO).toList());
        return dto;
    }
}
