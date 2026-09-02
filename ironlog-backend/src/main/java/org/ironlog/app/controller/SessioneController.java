package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.*;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.SessioneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/sessioni")
@RequiredArgsConstructor

public class SessioneController {

    private final SessioneService sessioneService;

    @PostMapping
    public ResponseEntity<SessioneResponseDTO> apri(@RequestBody SessioneRequestDTO dto, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(sessioneService.apri(dto, atleta));
    }

    @PostMapping("/{sessioneId}/serie")
    public ResponseEntity<SerieEseguitaResponseDTO> registraSerie(@PathVariable Long sessioneId,
                                                                  @Valid @RequestBody SerieEseguitaRequestDTO dto,
                                                                  Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(sessioneService.registraSerie(sessioneId, dto, atleta));
    }

    @PatchMapping("/{sessioneId}/conclusione")
    public ResponseEntity<RiepilogoSessioneDTO> concludi(@PathVariable Long sessioneId, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(sessioneService.concludi(sessioneId, atleta));
    }

    @GetMapping("/aperta")
    public ResponseEntity<SessioneResponseDTO> findAperta(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        SessioneResponseDTO sessione = sessioneService.findAperta(atleta);
        if (sessione == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sessione);
    }

    @GetMapping
    public ResponseEntity<List<SessioneSintesiDTO>> findStorico(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(sessioneService.findStorico(atleta));
    }

}
