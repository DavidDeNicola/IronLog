package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.VoceDiarioRequestDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.VoceDiarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/atleta/diario")
@RequiredArgsConstructor
public class VoceDiarioAtletaController {

    private final VoceDiarioService voceDiarioService;

    @PostMapping
    public ResponseEntity<VoceDiarioResponseDTO> registra(@Valid @RequestBody VoceDiarioRequestDTO dto, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(voceDiarioService.registra(dto, atleta));
    }

    @GetMapping
    public ResponseEntity<List<VoceDiarioResponseDTO>> findByData(@RequestParam LocalDate data, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(voceDiarioService.findByAtletaEData(atleta, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(@PathVariable Long id, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        voceDiarioService.elimina(id, atleta);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/riepilogo")
    public ResponseEntity<RiepilogoDiarioDTO> riepilogo(@RequestParam LocalDate data, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(voceDiarioService.riepilogo(atleta, data));
    }
}
