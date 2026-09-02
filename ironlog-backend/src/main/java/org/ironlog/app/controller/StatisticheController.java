package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.VolumeGruppoDTO;
import org.ironlog.app.model.PeriodoStatistica;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.StatisticheService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/atleta/statistiche")
@RequiredArgsConstructor

public class StatisticheController {

    private final StatisticheService statisticheService;

    @GetMapping("/volume")
    public ResponseEntity<List<VolumeGruppoDTO>> volumePerGruppo(@RequestParam PeriodoStatistica periodo, Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(statisticheService.volumePerGruppo(atleta, periodo));
    }
}
