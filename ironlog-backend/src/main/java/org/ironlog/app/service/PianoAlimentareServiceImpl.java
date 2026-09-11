package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.config.NutrizioneProperties;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.exception.FaseNonAttivaException;
import org.ironlog.app.exception.FaseNonTrovataException;
import org.ironlog.app.exception.PianoAlimentareNonTrovatoException;
import org.ironlog.app.mapper.PianoAlimentareMapper;
import org.ironlog.app.model.Fase;
import org.ironlog.app.model.PianoAlimentare;
import org.ironlog.app.model.Piatto;
import org.ironlog.app.model.TipoPasto;
import org.ironlog.app.model.Utente;
import org.ironlog.app.model.VocePianoAlimentare;
import org.ironlog.app.repository.FaseRepository;
import org.ironlog.app.repository.PianoAlimentareRepository;
import org.ironlog.app.repository.PiattoRepository;
import org.ironlog.app.service.definition.FaseService;
import org.ironlog.app.service.definition.PianoAlimentareService;
import org.ironlog.app.service.definition.VoceDiarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PianoAlimentareServiceImpl implements PianoAlimentareService {

    private static final BigDecimal CENTO = BigDecimal.valueOf(100);

    private final FaseRepository faseRepository;
    private final FaseService faseService;
    private final VoceDiarioService voceDiarioService;
    private final PiattoRepository piattoRepository;
    private final PianoAlimentareRepository pianoAlimentareRepository;
    private final PianoAlimentareMapper pianoAlimentareMapper;
    private final MotoreGenerazioneDieta motoreGenerazioneDieta;
    private final NutrizioneProperties nutrizioneProperties;

    @Override
    @Transactional
    public PianoAlimentareResponseDTO genera(Long faseId, Utente atleta) {
        Fase fase = faseRepository.findByIdAndAtleta(faseId, atleta)
                .orElseThrow(() -> new FaseNonTrovataException("Fase non trovata"));

        if (fase.getDataFine() != null) {
            throw new FaseNonAttivaException("La fase non è attiva");
        }

        FaseResponseDTO faseAttiva = faseService.findAttiva(atleta);
        RiepilogoDiarioDTO riepilogoOggi = voceDiarioService.riepilogo(atleta, LocalDate.now());

        TargetMacro targetResiduo = new TargetMacro(
                faseAttiva.getTargetCaloricoAttuale().subtract(riepilogoOggi.getCalorieTotali()),
                faseAttiva.getTargetProteineG().subtract(riepilogoOggi.getProteineTotali()),
                faseAttiva.getTargetGrassiG().subtract(riepilogoOggi.getGrassiTotali()),
                faseAttiva.getTargetCarboidratiG().subtract(riepilogoOggi.getCarboidratiTotali())
        );

        PianoAlimentare piano = new PianoAlimentare();
        piano.setFase(fase);
        piano.setDataGenerazione(LocalDate.now());

        boolean vincoliSoddisfatti = true;
        Set<Piatto> piattiUsatiOggi = new HashSet<>();

        for (Map.Entry<TipoPasto, BigDecimal> quotaPasto : quotePerPasto(nutrizioneProperties.pasti()).entrySet()) {
            TargetMacro targetPasto = applicaQuota(targetResiduo, quotaPasto.getValue());
            List<Piatto> piattiEligibili = piattoRepository.findByTipoPastoConIngredienti(quotaPasto.getKey());
            PianoGenerato generato = motoreGenerazioneDieta.genera(piattiEligibili, targetPasto, piattiUsatiOggi);
            piattiUsatiOggi.add(generato.piattoScelto());
            vincoliSoddisfatti = vincoliSoddisfatti && generato.vincoliSoddisfatti();

            for (VocePianoGenerata vocePianoGenerata : generato.voci()) {
                VocePianoAlimentare voce = new VocePianoAlimentare();
                voce.setPianoAlimentare(piano);
                voce.setAlimento(vocePianoGenerata.alimento());
                voce.setTipoPasto(quotaPasto.getKey());
                voce.setQuantitaGrammi(vocePianoGenerata.quantitaGrammi());
                piano.getVoci().add(voce);
            }
        }

        piano.setVincoliSoddisfatti(vincoliSoddisfatti);

        return pianoAlimentareMapper.toResponseDTO(pianoAlimentareRepository.save(piano));
    }

    @Override
    public List<PianoAlimentareSintesiDTO> findByFase(Long faseId, Utente atleta) {
        Fase fase = faseRepository.findByIdAndAtleta(faseId, atleta)
                .orElseThrow(() -> new FaseNonTrovataException("Fase non trovata"));

        return pianoAlimentareRepository.findByFaseOrderByDataGenerazioneDesc(fase).stream()
                .map(pianoAlimentareMapper::toSintesiDTO)
                .toList();
    }

    @Override
    public PianoAlimentareResponseDTO findById(Long id, Utente atleta) {
        PianoAlimentare piano = pianoAlimentareRepository.findByIdAndFaseAtleta(id, atleta)
                .orElseThrow(() -> new PianoAlimentareNonTrovatoException("Piano alimentare non trovato"));
        return pianoAlimentareMapper.toResponseDTO(piano);
    }

    private Map<TipoPasto, BigDecimal> quotePerPasto(NutrizioneProperties.Pasti pasti) {
        Map<TipoPasto, BigDecimal> quote = new LinkedHashMap<>();
        quote.put(TipoPasto.COLAZIONE, pasti.percentualeColazione());
        quote.put(TipoPasto.PRANZO, pasti.percentualePranzo());
        quote.put(TipoPasto.SPUNTINO, pasti.percentualeSpuntino());
        quote.put(TipoPasto.CENA, pasti.percentualeCena());
        return quote;
    }

    private TargetMacro applicaQuota(TargetMacro target, BigDecimal percentuale) {
        BigDecimal fattore = percentuale.divide(CENTO, 6, RoundingMode.HALF_UP);
        return new TargetMacro(
                target.calorie().multiply(fattore),
                target.proteineG().multiply(fattore),
                target.grassiG().multiply(fattore),
                target.carboidratiG().multiply(fattore)
        );
    }
}
