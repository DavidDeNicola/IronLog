package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.StatisticaClienteDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coach/statistiche")
@RequiredArgsConstructor
public class CoachStatisticheController {

    private final CoachService coachService;

    @GetMapping("/clienti")
    public ResponseEntity<List<StatisticaClienteDTO>> statisticheClienti(Authentication authentication) {
        Utente coach = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.statisticheClienti(coach));
    }
}