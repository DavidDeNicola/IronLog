package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/acqua")
@RequiredArgsConstructor
public class AcquaCoachController {

    private final CoachService coachService;

    @GetMapping
    public ResponseEntity<List<ConsumoAcquaResponseDTO>> findByData(@PathVariable Long atletaId, @RequestParam LocalDate data,
                                                                      Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findAcquaAtleta(atletaId, data, coach));
    }
}
