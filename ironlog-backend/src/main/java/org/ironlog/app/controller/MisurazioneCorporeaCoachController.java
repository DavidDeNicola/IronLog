package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/misurazioni")
@RequiredArgsConstructor
public class MisurazioneCorporeaCoachController {

    private final CoachService coachService;

    @PostMapping
    public ResponseEntity<MisurazioneCorporeaResponseDTO> create(@PathVariable Long atletaId,
                                                                   @Valid @RequestBody MisurazioneCorporeaRequestDTO dto,
                                                                   Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.createMisurazionePerAtleta(dto, atletaId, coach));
    }

    @GetMapping
    public ResponseEntity<List<MisurazioneCorporeaResponseDTO>> findAll(@PathVariable Long atletaId, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findMisurazioniAtleta(atletaId, coach));
    }
}
