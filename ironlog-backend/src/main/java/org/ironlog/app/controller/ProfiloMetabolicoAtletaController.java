package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.ProfiloMetabolicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atleta/profilo-metabolico")
@RequiredArgsConstructor
public class ProfiloMetabolicoAtletaController {

    private final ProfiloMetabolicoService profiloMetabolicoService;

    @PostMapping
    public ResponseEntity<ProfiloMetabolicoResponseDTO> calcola(@Valid @RequestBody ProfiloMetabolicoRequestDTO dto,
                                                                  Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(profiloMetabolicoService.calcola(dto, atleta));
    }

    @GetMapping("/attuale")
    public ResponseEntity<ProfiloMetabolicoResponseDTO> findAttuale(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(profiloMetabolicoService.findAttuale(atleta));
    }

    @GetMapping
    public ResponseEntity<List<ProfiloMetabolicoResponseDTO>> findStorico(Authentication authentication) {
        Utente atleta = (Utente) authentication.getPrincipal();
        return ResponseEntity.ok(profiloMetabolicoService.findStorico(atleta));
    }
}
