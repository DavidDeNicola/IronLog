package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.SchedaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/schede")
@RequiredArgsConstructor

public class SchedaAtletaController {

    private final SchedaService schedaService;

    @PostMapping
    public ResponseEntity<SchedaResponseDTO> create(@Valid @RequestBody SchedaRequestDTO dto, Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        SchedaResponseDTO creata = schedaService.create(dto, utente, utente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creata);
    }

    @GetMapping
    public ResponseEntity<List<SchedaSintesiDTO>> findAll(Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(schedaService.findByAtleta(utente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedaResponseDTO> findById(@PathVariable Long id, Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(schedaService.findByIdAndAtleta(id, utente));
    }
}
