package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.config.NutrizioneProperties;
import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.exception.FaseNonTrovataException;
import org.ironlog.app.exception.ProfiloMetabolicoNonTrovatoException;
import org.ironlog.app.mapper.FaseMapper;
import org.ironlog.app.model.Fase;
import org.ironlog.app.model.MisurazioneCorporea;
import org.ironlog.app.model.ProfiloMetabolico;
import org.ironlog.app.model.Sesso;
import org.ironlog.app.model.TipoFase;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.FaseRepository;
import org.ironlog.app.repository.ProfiloMetabolicoRepository;
import org.ironlog.app.service.definition.FaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaseServiceImpl implements FaseService {

    private final FaseRepository faseRepository;
    private final ProfiloMetabolicoRepository profiloMetabolicoRepository;
    private final FaseMapper faseMapper;
    private final CalcolatoreTargetFase calcolatoreTargetFase;
    private final CalcolatoreAdattamentoProgressivo calcolatoreAdattamentoProgressivo;
    private final CalcolatoreDurataCut calcolatoreDurataCut;
    private final NutrizioneProperties nutrizioneProperties;

    @Override
    @Transactional
    public FaseResponseDTO create(FaseRequestDTO dto, Utente atleta) {
        faseRepository.findByAtletaAndDataFineIsNull(atleta).ifPresent(precedente -> {
            precedente.setDataFine(dto.getDataInizio().minusDays(1));
            faseRepository.save(precedente);
        });

        ProfiloMetabolico profilo = profiloMetabolicoRepository.findFirstByAtletaOrderByCalcolatoIlDesc(atleta)
                .orElseThrow(() -> new ProfiloMetabolicoNonTrovatoException("Nessun profilo metabolico trovato"));

        BigDecimal pesoKg = profilo.getMisurazioneRiferimento().getPesoKg();

        BigDecimal calorieTarget;
        if (dto.getTargetCaloricoOverride() != null) {
            calorieTarget = dto.getTargetCaloricoOverride();
        } else if (dto.getTipo() == TipoFase.REVERSE_DIET) {
            calorieTarget = faseRepository.findFirstByAtletaAndDataFineIsNotNullOrderByDataFineDesc(atleta)
                    .map(Fase::getBaseTargetCalorico)
                    .orElseGet(() -> calcolatoreTargetFase.calcolaCalorieTarget(profilo.getTdee(), TipoFase.MAINTENANCE, atleta.getSesso(), nutrizioneProperties));
        } else {
            calorieTarget = calcolatoreTargetFase.calcolaCalorieTarget(profilo.getTdee(), dto.getTipo(), atleta.getSesso(), nutrizioneProperties);
        }

        TargetMacro macro = calcolatoreTargetFase.calcolaMacro(calorieTarget, pesoKg, nutrizioneProperties.macro());

        Fase fase = new Fase();
        fase.setAtleta(atleta);
        fase.setProfiloMetabolicoRiferimento(profilo);
        fase.setTipo(dto.getTipo());
        fase.setDataInizio(dto.getDataInizio());
        fase.setBaseTargetCalorico(calorieTarget);
        fase.setTargetProteineG(dto.getTargetProteineGOverride() != null ? dto.getTargetProteineGOverride() : macro.proteineG());
        fase.setTargetGrassiG(dto.getTargetGrassiGOverride() != null ? dto.getTargetGrassiGOverride() : macro.grassiG());
        fase.setTargetCarboidratiG(dto.getTargetCarboidratiGOverride() != null ? dto.getTargetCarboidratiGOverride() : macro.carboidratiG());

        Fase salvata = faseRepository.save(fase);
        return toResponseDTO(salvata, durataSuggeritaPer(atleta, salvata.getTipo()), atleta.getSesso());
    }

    @Override
    public FaseResponseDTO findAttiva(Utente atleta) {
        Fase fase = faseRepository.findByAtletaAndDataFineIsNull(atleta)
                .orElseThrow(() -> new FaseNonTrovataException("Nessuna fase attiva trovata"));
        return toResponseDTO(fase, durataSuggeritaPer(atleta, fase.getTipo()), atleta.getSesso());
    }

    @Override
    public List<FaseSintesiDTO> findByAtleta(Utente atleta) {
        return faseRepository.findByAtletaOrderByDataInizioDesc(atleta).stream()
                .map(faseMapper::toSintesiDTO)
                .toList();
    }

    @Override
    @Transactional
    public FaseResponseDTO chiudi(Long id, Utente atleta) {
        Fase fase = faseRepository.findByIdAndAtleta(id, atleta)
                .orElseThrow(() -> new FaseNonTrovataException("Fase non trovata"));
        fase.setDataFine(LocalDate.now());
        Fase salvata = faseRepository.save(fase);
        return toResponseDTO(salvata, durataSuggeritaPer(atleta, salvata.getTipo()), atleta.getSesso());
    }

    private FaseResponseDTO toResponseDTO(Fase fase, Integer durataSuggeritaMesi, Sesso sesso) {
        LocalDate fine = fase.getDataFine() != null ? fase.getDataFine() : LocalDate.now();
        long giorniTrascorsi = ChronoUnit.DAYS.between(fase.getDataInizio(), fine);

        ParametriAdattamento parametri = parametriPer(fase.getTipo());
        BigDecimal calorieMinime = calorieMinimePer(sesso);
        BigDecimal targetAttuale = calcolatoreAdattamentoProgressivo.calcolaTargetAttuale(
                fase.getBaseTargetCalorico(), parametri.intervalDays(), parametri.stepKcal(), parametri.direction(),
                giorniTrascorsi, calorieMinime);

        return faseMapper.toResponseDTO(fase, targetAttuale, giorniTrascorsi, durataSuggeritaMesi);
    }

    private BigDecimal calorieMinimePer(Sesso sesso) {
        if (sesso == null) {
            return null;
        }
        return sesso == Sesso.FEMMINA
                ? nutrizioneProperties.sicurezza().calorieMinimeFemmina()
                : nutrizioneProperties.sicurezza().calorieMinimeMaschio();
    }

    private Integer durataSuggeritaPer(Utente atleta, TipoFase tipo) {
        if (tipo != TipoFase.CUT) {
            return null;
        }
        return faseRepository.findFirstByAtletaAndTipoAndDataFineIsNotNullOrderByDataFineDesc(atleta, TipoFase.BULK)
                .map(Fase::getProfiloMetabolicoRiferimento)
                .map(ProfiloMetabolico::getMisurazioneRiferimento)
                .map(MisurazioneCorporea::getPercentualeMassaGrassa)
                .map(bf -> calcolatoreDurataCut.suggerisciDurataMesi(bf, nutrizioneProperties.cut()))
                .orElse(null);
    }

    private ParametriAdattamento parametriPer(TipoFase tipo) {
        return switch (tipo) {
            case CUT -> new ParametriAdattamento(nutrizioneProperties.cut().adjustmentIntervalDays(),
                    nutrizioneProperties.cut().adjustmentStepKcal(), -1);
            case BULK -> new ParametriAdattamento(nutrizioneProperties.bulk().adjustmentIntervalDays(),
                    nutrizioneProperties.bulk().adjustmentStepKcal(), 1);
            case REVERSE_DIET -> new ParametriAdattamento(nutrizioneProperties.reverseDiet().adjustmentIntervalDays(),
                    nutrizioneProperties.reverseDiet().adjustmentStepKcal(), 1);
            case MAINTENANCE, MINI_CUT, DIET_BREAK -> new ParametriAdattamento(0, BigDecimal.ZERO, 0);
        };
    }

    private record ParametriAdattamento(int intervalDays, BigDecimal stepKcal, int direction) {
    }
}
