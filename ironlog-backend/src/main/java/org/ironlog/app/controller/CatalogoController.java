package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor

public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping("/esercizi")
    public ResponseEntity<List<EsercizioResponseDTO>> getAllEsercizi() {
        return ResponseEntity.ok(catalogoService.findAllEsercizi());
    }

    @GetMapping("/gruppi")
    public ResponseEntity<List<GruppoMuscolareResponseDTO>> getAllGruppiMuscolari() {
        return ResponseEntity.ok(catalogoService.findAllGruppi());
    }

    @GetMapping("/gruppi/{id}/esercizi")
    public ResponseEntity<List<EsercizioResponseDTO>> getEserciziByGruppo(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.findAllEserciziByGruppo(id));
    }

    @GetMapping("/esercizi/cerca")
    public ResponseEntity<List<EsercizioResponseDTO>> cerca(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long gruppoId) {
        return ResponseEntity.ok(catalogoService.cerca(nome, gruppoId));
    }

    @PostMapping("/esercizi/{id}/preferito")
    public ResponseEntity<Void> aggiungiPreferito(@PathVariable Long id, Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        catalogoService.aggiungiPreferito(id, utente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/esercizi/{id}/preferito")
    public ResponseEntity<Void> rimuoviPreferito(@PathVariable Long id, Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        catalogoService.rimuoviPreferito(id, utente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/esercizi/preferiti")
    public ResponseEntity<List<EsercizioResponseDTO>> findPreferiti(Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(catalogoService.findPreferiti(utente));
    }
}
