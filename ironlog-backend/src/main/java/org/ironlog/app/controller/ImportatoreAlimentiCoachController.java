package org.ironlog.app.controller;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.RisultatoImportDTO;
import org.ironlog.app.service.definition.ImportatoreAlimentiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/coach/alimenti")
@RequiredArgsConstructor
public class ImportatoreAlimentiCoachController {

    private final ImportatoreAlimentiService importatoreAlimentiService;

    @PostMapping("/import")
    public ResponseEntity<RisultatoImportDTO> importa(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(importatoreAlimentiService.importa(file));
    }
}
