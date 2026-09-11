package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.PianoAlimentareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/piani-alimentari")
@RequiredArgsConstructor
public class PianoAlimentareAtletaController {

    private final PianoAlimentareService pianoAlimentareService;

    @PostMapping("/genera")
    public ResponseEntity<PianoAlimentareResponseDTO> genera(@RequestParam Long faseId, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(pianoAlimentareService.genera(faseId, atleta));
    }

    @GetMapping
    public ResponseEntity<List<PianoAlimentareSintesiDTO>> findByFase(@RequestParam Long faseId, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(pianoAlimentareService.findByFase(faseId, atleta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PianoAlimentareResponseDTO> findById(@PathVariable Long id, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(pianoAlimentareService.findById(id, atleta));
    }
}
