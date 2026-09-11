package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.exception.DatiBiometriciMancantiException;
import org.ironlog.app.exception.MisurazioneNonTrovataException;
import org.ironlog.app.exception.ProfiloMetabolicoNonTrovatoException;
import org.ironlog.app.mapper.ProfiloMetabolicoMapper;
import org.ironlog.app.model.FormulaMetabolica;
import org.ironlog.app.model.MisurazioneCorporea;
import org.ironlog.app.model.ProfiloMetabolico;
import org.ironlog.app.model.Utente;
import org.ironlog.app.repository.MisurazioneCorporeaRepository;
import org.ironlog.app.repository.ProfiloMetabolicoRepository;
import org.ironlog.app.service.definition.ProfiloMetabolicoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfiloMetabolicoServiceImpl implements ProfiloMetabolicoService {

    private final MisurazioneCorporeaRepository misurazioneCorporeaRepository;
    private final ProfiloMetabolicoRepository profiloMetabolicoRepository;
    private final ProfiloMetabolicoMapper profiloMetabolicoMapper;
    private final CalcolatoreMetabolico calcolatoreMetabolico;

    @Override
    public ProfiloMetabolicoResponseDTO calcola(ProfiloMetabolicoRequestDTO dto, Utente atleta) {
        if (atleta.getSesso() == null || atleta.getDataNascita() == null) {
            throw new DatiBiometriciMancantiException("Dati biometrici (sesso e data di nascita) mancanti");
        }

        MisurazioneCorporea misurazione = misurazioneCorporeaRepository.findFirstByAtletaOrderByDataDesc(atleta)
                .orElseThrow(() -> new MisurazioneNonTrovataException("Nessuna misurazione corporea trovata"));

        FormulaMetabolica formula = dto.getFormulaForzata() != null
                ? dto.getFormulaForzata()
                : misurazione.getPercentualeMassaGrassa() != null
                        ? FormulaMetabolica.KATCH_MCARDLE
                        : FormulaMetabolica.MIFFLIN_ST_JEOR;

        int eta = Period.between(atleta.getDataNascita(), LocalDate.now()).getYears();

        BigDecimal bmr = formula == FormulaMetabolica.KATCH_MCARDLE
                ? calcolatoreMetabolico.calcolaBmrKatchMcArdle(misurazione.getPesoKg(), misurazione.getPercentualeMassaGrassa())
                : calcolatoreMetabolico.calcolaBmrMifflin(atleta.getSesso(), misurazione.getPesoKg(), BigDecimal.valueOf(misurazione.getAltezzaCm()), eta);

        BigDecimal tdee = calcolatoreMetabolico.calcolaTdee(bmr, dto.getLivelloAttivita().getFattore());

        ProfiloMetabolico profilo = new ProfiloMetabolico();
        profilo.setAtleta(atleta);
        profilo.setMisurazioneRiferimento(misurazione);
        profilo.setCalcolatoIl(LocalDate.now());
        profilo.setBmr(bmr);
        profilo.setTdee(tdee);
        profilo.setLivelloAttivita(dto.getLivelloAttivita());
        profilo.setFormula(formula);

        return profiloMetabolicoMapper.toResponseDTO(profiloMetabolicoRepository.save(profilo));
    }

    @Override
    public ProfiloMetabolicoResponseDTO findAttuale(Utente atleta) {
        ProfiloMetabolico profilo = profiloMetabolicoRepository.findFirstByAtletaOrderByCalcolatoIlDesc(atleta)
                .orElseThrow(() -> new ProfiloMetabolicoNonTrovatoException("Nessun profilo metabolico trovato"));
        return profiloMetabolicoMapper.toResponseDTO(profilo);
    }

    @Override
    public List<ProfiloMetabolicoResponseDTO> findStorico(Utente atleta) {
        return profiloMetabolicoRepository.findByAtletaOrderByCalcolatoIlDesc(atleta).stream()
                .map(profiloMetabolicoMapper::toResponseDTO)
                .toList();
    }
}
