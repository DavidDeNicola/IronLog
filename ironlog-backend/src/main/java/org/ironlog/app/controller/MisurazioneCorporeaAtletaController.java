package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.MisurazioneCorporeaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/misurazioni")
@RequiredArgsConstructor
public class MisurazioneCorporeaAtletaController {

    private final MisurazioneCorporeaService misurazioneCorporeaService;

    @PostMapping
    public ResponseEntity<MisurazioneCorporeaResponseDTO> registra(@Valid @RequestBody MisurazioneCorporeaRequestDTO dto,
                                                                     Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(misurazioneCorporeaService.registra(dto, atleta));
    }

    @GetMapping
    public ResponseEntity<List<MisurazioneCorporeaResponseDTO>> findAll(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(misurazioneCorporeaService.findByAtleta(atleta));
    }

    @GetMapping("/ultima")
    public ResponseEntity<MisurazioneCorporeaResponseDTO> findUltima(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(misurazioneCorporeaService.findUltima(atleta));
    }
}
