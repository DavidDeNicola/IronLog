package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/piani-alimentari")
@RequiredArgsConstructor
public class PianoAlimentareCoachController {

    private final CoachService coachService;

    @PostMapping("/genera")
    public ResponseEntity<PianoAlimentareResponseDTO> genera(@PathVariable Long atletaId, @RequestParam Long faseId,
                                                               Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.generaPianoAlimentarePerAtleta(faseId, atletaId, coach));
    }

    @GetMapping
    public ResponseEntity<List<PianoAlimentareSintesiDTO>> findByFase(@PathVariable Long atletaId, @RequestParam Long faseId,
                                                                        Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findPianiAlimentariAtleta(faseId, atletaId, coach));
    }
}
