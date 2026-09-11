package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.AssunzioneIntegratoreRequestDTO;
import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.AssunzioneIntegratoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/atleta/integratori/assunzioni")
@RequiredArgsConstructor
public class AssunzioneIntegratoreAtletaController {

    private final AssunzioneIntegratoreService assunzioneIntegratoreService;

    @PostMapping
    public ResponseEntity<AssunzioneIntegratoreResponseDTO> registra(@Valid @RequestBody AssunzioneIntegratoreRequestDTO dto,
                                                                       Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(assunzioneIntegratoreService.registra(dto, atleta));
    }

    @GetMapping
    public ResponseEntity<List<AssunzioneIntegratoreResponseDTO>> findByData(@RequestParam LocalDate data, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(assunzioneIntegratoreService.findByAtletaEData(atleta, data));
    }
}
