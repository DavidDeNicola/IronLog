package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.FaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/fasi")
@RequiredArgsConstructor
public class FaseAtletaController {

    private final FaseService faseService;

    @PostMapping
    public ResponseEntity<FaseResponseDTO> create(@Valid @RequestBody FaseRequestDTO dto, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(faseService.create(dto, atleta));
    }

    @GetMapping
    public ResponseEntity<List<FaseSintesiDTO>> findAll(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(faseService.findByAtleta(atleta));
    }

    @GetMapping("/attiva")
    public ResponseEntity<FaseResponseDTO> findAttiva(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(faseService.findAttiva(atleta));
    }

    @PutMapping("/{id}/chiudi")
    public ResponseEntity<FaseResponseDTO> chiudi(@PathVariable Long id, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(faseService.chiudi(id, atleta));
    }
}
