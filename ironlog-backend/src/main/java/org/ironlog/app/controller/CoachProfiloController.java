package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ProfiloResponseDTO;
import org.ironlog.app.model.Utente;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coach/profilo")
@RequiredArgsConstructor
public class CoachProfiloController {

    @GetMapping
    public ResponseEntity<ProfiloResponseDTO> profilo(Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();

        ProfiloResponseDTO dto = new ProfiloResponseDTO();
        dto.setId(utente.getId());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setEmail(utente.getEmail());
        dto.setRuolo(utente.getRuolo());
        return ResponseEntity.ok(dto);
    }
}