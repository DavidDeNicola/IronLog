package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ConsumoAcquaRequestDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.ConsumoAcquaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/atleta/acqua")
@RequiredArgsConstructor
public class ConsumoAcquaAtletaController {

    private final ConsumoAcquaService consumoAcquaService;

    @PostMapping
    public ResponseEntity<ConsumoAcquaResponseDTO> registra(@Valid @RequestBody ConsumoAcquaRequestDTO dto, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(consumoAcquaService.registra(dto, atleta));
    }

    @GetMapping
    public ResponseEntity<List<ConsumoAcquaResponseDTO>> findByData(@RequestParam LocalDate data, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(consumoAcquaService.findByAtletaEData(atleta, data));
    }
}
