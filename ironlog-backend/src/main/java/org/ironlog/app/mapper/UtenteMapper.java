package org.ironlog.app.mapper;

import org.ironlog.app.dto.RegisterRequestDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class UtenteMapper {

    public Utente toEntity(RegisterRequestDTO dto) {
        Utente utente = new Utente();

        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setEmail(dto.getEmail());
        utente.setRuolo(dto.getRuolo());

        return utente;
    }

    public UtenteResponseDTO toResponseDTO(Utente utente){
        UtenteResponseDTO dto = new UtenteResponseDTO();
        dto.setId(utente.getId());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setEmail(utente.getEmail());
        return dto;
    }
}
