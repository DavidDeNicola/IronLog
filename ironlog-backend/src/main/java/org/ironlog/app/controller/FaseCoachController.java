package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/fasi")
@RequiredArgsConstructor
public class FaseCoachController {

    private final CoachService coachService;

    @PostMapping
    public ResponseEntity<FaseResponseDTO> create(@PathVariable Long atletaId,
                                                    @Valid @RequestBody FaseRequestDTO dto,
                                                    Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.createFasePerAtleta(dto, atletaId, coach));
    }

    @GetMapping
    public ResponseEntity<List<FaseSintesiDTO>> findAll(@PathVariable Long atletaId, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findFasiAtleta(atletaId, coach));
    }

    @GetMapping("/attiva")
    public ResponseEntity<FaseResponseDTO> findAttiva(@PathVariable Long atletaId, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findFaseAttivaAtleta(atletaId, coach));
    }

    @PutMapping("/{id}/chiudi")
    public ResponseEntity<FaseResponseDTO> chiudi(@PathVariable Long atletaId, @PathVariable Long id, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.chiudiFasePerAtleta(id, atletaId, coach));
    }
}
