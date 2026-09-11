package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.RisultatoImportDTO;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.FonteAlimento;
import org.ironlog.app.repository.AlimentoRepository;
import org.ironlog.app.service.definition.ImportatoreAlimentiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Import una tantum di alimenti da CSV esterno (formato "flat": nome + macro/micro
 * per 100g). Nessuna libreria di parsing CSV: split manuale con gestione minimale
 * delle virgolette per i nomi che contengono virgole. Gli errori riga per riga
 * vengono raccolti nel risultato, non propagati (comportamento best-effort).
 */
@Service
@RequiredArgsConstructor
public class ImportatoreAlimentiServiceImpl implements ImportatoreAlimentiService {

    private static final int COLONNE_ATTESE = 8;

    private final AlimentoRepository alimentoRepository;

    @Override
    @Transactional
    public RisultatoImportDTO importa(MultipartFile file) throws IOException {
        RisultatoImportDTO risultato = new RisultatoImportDTO();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String intestazione = reader.readLine();
            if (intestazione == null) {
                risultato.getErrori().add("File vuoto");
                return risultato;
            }

            String riga;
            int numeroRiga = 1;
            while ((riga = reader.readLine()) != null) {
                numeroRiga++;
                if (riga.isBlank()) {
                    continue;
                }
                risultato.setRigheLette(risultato.getRigheLette() + 1);

                try {
                    processaRiga(riga, risultato);
                } catch (RuntimeException e) {
                    risultato.setRigheScartate(risultato.getRigheScartate() + 1);
                    risultato.getErrori().add("Riga " + numeroRiga + ": " + e.getMessage());
                }
            }
        }

        return risultato;
    }

    private void processaRiga(String riga, RisultatoImportDTO risultato) {
        List<String> campi = parseRigaCsv(riga);
        if (campi.size() < COLONNE_ATTESE) {
            throw new IllegalArgumentException("attese " + COLONNE_ATTESE + " colonne, trovate " + campi.size());
        }

        String nome = campi.get(0).trim();
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("nome mancante");
        }

        BigDecimal calorie100g = numeroObbligatorio(campi.get(1), "calorie100g");
        BigDecimal proteineG = numeroObbligatorio(campi.get(2), "proteineG");
        BigDecimal grassiG = numeroObbligatorio(campi.get(3), "grassiG");
        BigDecimal carboidratiG = numeroObbligatorio(campi.get(4), "carboidratiG");
        BigDecimal fibreG = numeroOpzionale(campi.get(5));
        BigDecimal zuccheriG = numeroOpzionale(campi.get(6));
        BigDecimal sodioMg = numeroOpzionale(campi.get(7));

        Optional<Alimento> esistente = alimentoRepository.findByNomeIgnoreCase(nome);

        if (esistente.isPresent()) {
            Alimento alimento = esistente.get();
            if (alimento.getFonte() != FonteAlimento.IMPORT_ESTERNO) {
                risultato.setRigheScartate(risultato.getRigheScartate() + 1);
                risultato.getErrori().add("'" + nome + "': esiste già come alimento personalizzato, riga scartata");
                return;
            }
            aggiornaCampi(alimento, calorie100g, proteineG, grassiG, carboidratiG, fibreG, zuccheriG, sodioMg);
            alimentoRepository.save(alimento);
            risultato.setAlimentiAggiornati(risultato.getAlimentiAggiornati() + 1);
        } else {
            Alimento alimento = new Alimento();
            alimento.setNome(nome);
            alimento.setFonte(FonteAlimento.IMPORT_ESTERNO);
            aggiornaCampi(alimento, calorie100g, proteineG, grassiG, carboidratiG, fibreG, zuccheriG, sodioMg);
            alimentoRepository.save(alimento);
            risultato.setAlimentiCreati(risultato.getAlimentiCreati() + 1);
        }
    }

    private void aggiornaCampi(Alimento alimento, BigDecimal calorie100g, BigDecimal proteineG, BigDecimal grassiG,
                                BigDecimal carboidratiG, BigDecimal fibreG, BigDecimal zuccheriG, BigDecimal sodioMg) {
        alimento.setCalorie100g(calorie100g);
        alimento.setProteineG(proteineG);
        alimento.setGrassiG(grassiG);
        alimento.setCarboidratiG(carboidratiG);
        alimento.setFibreG(fibreG);
        alimento.setZuccheriG(zuccheriG);
        alimento.setSodioMg(sodioMg);
    }

    private BigDecimal numeroObbligatorio(String valore, String nomeCampo) {
        BigDecimal numero = numeroOpzionale(valore);
        if (numero == null) {
            throw new IllegalArgumentException(nomeCampo + " mancante");
        }
        return numero;
    }

    private BigDecimal numeroOpzionale(String valore) {
        String pulito = valore == null ? "" : valore.trim();
        if (pulito.isEmpty()) {
            return null;
        }
        return new BigDecimal(pulito);
    }

    private List<String> parseRigaCsv(String riga) {
        List<String> campi = new ArrayList<>();
        StringBuilder corrente = new StringBuilder();
        boolean dentroVirgolette = false;

        for (int i = 0; i < riga.length(); i++) {
            char c = riga.charAt(i);
            if (c == '"') {
                dentroVirgolette = !dentroVirgolette;
            } else if (c == ',' && !dentroVirgolette) {
                campi.add(corrente.toString());
                corrente.setLength(0);
            } else {
                corrente.append(c);
            }
        }
        campi.add(corrente.toString());
        return campi;
    }
}
