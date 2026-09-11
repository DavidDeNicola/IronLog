package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.AlimentoRequestDTO;
import org.ironlog.app.dto.AlimentoResponseDTO;
import org.ironlog.app.service.definition.AlimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo/alimenti")
@RequiredArgsConstructor
public class AlimentoController {

    private final AlimentoService alimentoService;

    @GetMapping
    public ResponseEntity<List<AlimentoResponseDTO>> findAll() {
        return ResponseEntity.ok(alimentoService.findAll());
    }

    @GetMapping("/cerca")
    public ResponseEntity<List<AlimentoResponseDTO>> cerca(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(alimentoService.cerca(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alimentoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AlimentoResponseDTO> create(@Valid @RequestBody AlimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alimentoService.creaPersonalizzato(dto));
    }
}
