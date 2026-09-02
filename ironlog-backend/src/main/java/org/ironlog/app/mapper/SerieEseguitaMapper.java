package org.ironlog.app.mapper;

import org.ironlog.app.dto.SerieEseguitaRequestDTO;
import org.ironlog.app.dto.SerieEseguitaResponseDTO;
import org.ironlog.app.model.Esercizio;
import org.ironlog.app.model.SerieEseguita;
import org.ironlog.app.model.Sessione;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component

public class SerieEseguitaMapper {

    public SerieEseguitaResponseDTO toResponseDTO(SerieEseguita serieE) {
        SerieEseguitaResponseDTO dto = new SerieEseguitaResponseDTO();
        dto.setId(serieE.getId());
        dto.setNumeroSerie(serieE.getNumeroSerie());
        dto.setEsercizioNome(serieE.getEsercizio().getNome());
        dto.setRipetizioni(serieE.getRipetizioni());
        dto.setPeso(serieE.getPeso());
        dto.setRipObiettivo(serieE.getRipObiettivo());
        dto.setMassimaleStimato(serieE.getMassimaleStimato());
        return dto;
    }

    public SerieEseguita toEntity(SerieEseguitaRequestDTO dto, Sessione sessione, Esercizio esercizio,
                                  int numeroSerie, Integer ripObiettivo, BigDecimal massimaleStimato){

        SerieEseguita serieEseguita = new SerieEseguita();
        serieEseguita.setSessione(sessione);
        serieEseguita.setEsercizio(esercizio);
        serieEseguita.setNumeroSerie(numeroSerie);
        serieEseguita.setRipetizioni(dto.getRipetizioni());
        serieEseguita.setPeso(dto.getPeso());
        serieEseguita.setRipObiettivo(ripObiettivo);
        serieEseguita.setMassimaleStimato(massimaleStimato);
        return serieEseguita;
    }
}
