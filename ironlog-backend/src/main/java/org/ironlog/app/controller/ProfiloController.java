package org.ironlog.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.CambioPasswordRequestDTO;
import org.ironlog.app.dto.DatiBiometriciRequestDTO;
import org.ironlog.app.dto.ProfiloResponseDTO;
import org.ironlog.app.model.Utente;
import org.ironlog.app.service.definition.ProfiloService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atleta/profilo")
@RequiredArgsConstructor
public class ProfiloController {

    private final ProfiloService profiloService;

    @GetMapping
    public ResponseEntity<ProfiloResponseDTO> profilo(Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();

        ProfiloResponseDTO dto = new ProfiloResponseDTO();
        dto.setId(utente.getId());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setEmail(utente.getEmail());
        dto.setRuolo(utente.getRuolo());
        dto.setSesso(utente.getSesso());
        dto.setDataNascita(utente.getDataNascita());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/dati-biometrici")
    public ResponseEntity<Void> aggiornaDatiBiometrici(Authentication authentication,
                                                        @Valid @RequestBody DatiBiometriciRequestDTO request) {
        Utente utente = (Utente) authentication.getPrincipal();
        profiloService.aggiornaDatiBiometrici(utente, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiaPassword(Authentication authentication,
                                               @Valid @RequestBody CambioPasswordRequestDTO request) {
        Utente utente = (Utente) authentication.getPrincipal();
        profiloService.cambiaPassword(utente, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminaProfilo(Authentication authentication) {
        Utente utente = (Utente) authentication.getPrincipal();
        profiloService.eliminaProfilo(utente);
        return ResponseEntity.noContent().build();
    }
}