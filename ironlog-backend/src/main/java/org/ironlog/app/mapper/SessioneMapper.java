package org.ironlog.app.mapper;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SessioneResponseDTO;
import org.ironlog.app.dto.SessioneSintesiDTO;
import org.ironlog.app.model.Sessione;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class SessioneMapper {

    private final SerieEseguitaMapper serieEseguitaMapper;

    public SessioneResponseDTO toResponseDTO(Sessione sessione) {
        SessioneResponseDTO dto = new SessioneResponseDTO();
        dto.setId(sessione.getId());
        dto.setEseguitaIl(sessione.getEseguitaIl());
        dto.setConclusaIl(sessione.getConclusaIl());
        dto.setNote(sessione.getNote());
        dto.setSerie(sessione.getSerie().stream().map(serieEseguitaMapper::toResponseDTO).toList());
        dto.setGiornoNome(hasGiorno(sessione) ? sessione.getGiornoScheda().getNome() : null);
        dto.setSchedaNome(hasGiorno(sessione) ? sessione.getGiornoScheda().getScheda().getNome() : null);
        dto.setGiornoSchedaId(hasGiorno(sessione) ? sessione.getGiornoScheda().getId() : null);
        dto.setSchedaId(hasGiorno(sessione) ? sessione.getGiornoScheda().getScheda().getId() : null);
        return dto;
    }

    public SessioneSintesiDTO toSintesiDTO(Sessione sessione) {
        SessioneSintesiDTO dto = new SessioneSintesiDTO();
        dto.setId(sessione.getId());
        dto.setEseguitaIl(sessione.getEseguitaIl());
        dto.setConclusaIl(sessione.getConclusaIl());
        dto.setGiornoNome(hasGiorno(sessione) ? sessione.getGiornoScheda().getNome() : null);
        dto.setNumeroSerie(sessione.getSerie().size());
        return dto;
    }

    private boolean hasGiorno(Sessione sessione) {
        return sessione.getGiornoScheda() != null;
    }
}
