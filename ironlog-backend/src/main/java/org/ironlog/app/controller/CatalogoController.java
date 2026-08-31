package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.EsercizioResponseDTO;
import org.ironlog.app.dto.GruppoMuscolareResponseDTO;
import org.ironlog.app.service.definition.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
