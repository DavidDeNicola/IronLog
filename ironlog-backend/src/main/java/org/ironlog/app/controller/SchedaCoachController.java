package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach")
@RequiredArgsConstructor

public class SchedaCoachController {

    private final CoachService coachService;

    @GetMapping("/atleti")
    public ResponseEntity<List<UtenteResponseDTO>> findAtleti(Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findAtleti(coach));
    }

    @GetMapping("/atleti/{atletaId}/schede")
    public ResponseEntity<List<SchedaSintesiDTO>> findSchedeAtleta(@PathVariable Long atletaId, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findSchedeAtleta(atletaId, coach));
    }

    @PostMapping("/atleti/{atletaId}/schede")
    public ResponseEntity<SchedaResponseDTO> createSchedaPerAtleta(@PathVariable Long atletaId, @Valid @RequestBody SchedaRequestDTO dto, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.createSchedaPerAtleta(dto, atletaId, coach));
    }
}
