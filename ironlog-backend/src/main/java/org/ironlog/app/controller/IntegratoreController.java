package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.IntegratoreRequestDTO;
import org.ironlog.app.dto.IntegratoreResponseDTO;
import org.ironlog.app.service.definition.IntegratoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo/integratori")
@RequiredArgsConstructor
public class IntegratoreController {

    private final IntegratoreService integratoreService;

    @GetMapping
    public ResponseEntity<List<IntegratoreResponseDTO>> findAll() {
        return ResponseEntity.ok(integratoreService.findAll());
    }

    @PostMapping
    public ResponseEntity<IntegratoreResponseDTO> create(@Valid @RequestBody IntegratoreRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(integratoreService.create(dto));
    }
}
