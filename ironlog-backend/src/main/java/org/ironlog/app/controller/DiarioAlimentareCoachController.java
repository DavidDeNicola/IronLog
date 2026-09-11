package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/diario")
@RequiredArgsConstructor
public class DiarioAlimentareCoachController {

    private final CoachService coachService;

    @GetMapping
    public ResponseEntity<List<VoceDiarioResponseDTO>> findByData(@PathVariable Long atletaId, @RequestParam LocalDate data,
                                                                    Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findDiarioAtleta(atletaId, data, coach));
    }

    @GetMapping("/riepilogo")
    public ResponseEntity<RiepilogoDiarioDTO> riepilogo(@PathVariable Long atletaId, @RequestParam LocalDate data,
                                                          Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.riepilogoDiarioAtleta(atletaId, data, coach));
    }
}
