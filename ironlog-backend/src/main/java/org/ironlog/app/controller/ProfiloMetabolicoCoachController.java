package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach/atleti/{atletaId}/profilo-metabolico")
@RequiredArgsConstructor
public class ProfiloMetabolicoCoachController {

    private final CoachService coachService;

    @PostMapping
    public ResponseEntity<ProfiloMetabolicoResponseDTO> calcola(@PathVariable Long atletaId,
                                                                  @Valid @RequestBody ProfiloMetabolicoRequestDTO dto,
                                                                  Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(coachService.calcolaProfiloMetabolicoPerAtleta(dto, atletaId, coach));
    }

    @GetMapping("/attuale")
    public ResponseEntity<ProfiloMetabolicoResponseDTO> findAttuale(@PathVariable Long atletaId, Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.findProfiloMetabolicoAttualeAtleta(atletaId, coach));
    }
}
