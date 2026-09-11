package org.ironlog.app.service;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.VoceDiarioRequestDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.exception.AlimentoNonTrovatoException;
import org.ironlog.app.exception.FaseNonTrovataException;
import org.ironlog.app.exception.VoceDiarioNonTrovataException;
import org.ironlog.app.mapper.VoceDiarioMapper;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.Utente;
import org.ironlog.app.model.VoceDiario;
import org.ironlog.app.repository.AlimentoRepository;
import org.ironlog.app.repository.VoceDiarioRepository;
import org.ironlog.app.service.definition.FaseService;
import org.ironlog.app.service.definition.VoceDiarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoceDiarioServiceImpl implements VoceDiarioService {

    private final VoceDiarioRepository voceDiarioRepository;
    private final AlimentoRepository alimentoRepository;
    private final VoceDiarioMapper voceDiarioMapper;
    private final CalcolatoreRiepilogoDiario calcolatoreRiepilogoDiario;
    private final FaseService faseService;

    @Override
    public VoceDiarioResponseDTO registra(VoceDiarioRequestDTO dto, Utente atleta) {
        Alimento alimento = alimentoRepository.findById(dto.getAlimentoId())
                .orElseThrow(() -> new AlimentoNonTrovatoException("Alimento non trovato"));

        VoceDiario voce = voceDiarioMapper.toEntity(dto, atleta, alimento);
        return voceDiarioMapper.toResponseDTO(voceDiarioRepository.save(voce));
    }

    @Override
    public List<VoceDiarioResponseDTO> findByAtletaEData(Utente atleta, LocalDate data) {
        return voceDiarioRepository.findByAtletaAndData(atleta, data).stream()
                .map(voceDiarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void elimina(Long id, Utente atleta) {
        VoceDiario voce = voceDiarioRepository.findByIdAndAtleta(id, atleta)
                .orElseThrow(() -> new VoceDiarioNonTrovataException("Voce diario non trovata"));
        voceDiarioRepository.delete(voce);
    }

    @Override
    public RiepilogoDiarioDTO riepilogo(Utente atleta, LocalDate data) {
        List<VoceDiario> voci = voceDiarioRepository.findByAtletaAndData(atleta, data);
        List<VoceDiarioCalcolata> calcolate = voci.stream().map(voceDiarioMapper::calcola).toList();
        TargetMacro totali = calcolatoreRiepilogoDiario.somma(calcolate);

        RiepilogoDiarioDTO dto = new RiepilogoDiarioDTO();
        dto.setData(data);
        dto.setCalorieTotali(totali.calorie());
        dto.setProteineTotali(totali.proteineG());
        dto.setGrassiTotali(totali.grassiG());
        dto.setCarboidratiTotali(totali.carboidratiG());

        try {
            FaseResponseDTO faseAttiva = faseService.findAttiva(atleta);
            dto.setTargetCalorico(faseAttiva.getTargetCaloricoAttuale());
            dto.setDifferenzaCalorica(totali.calorie().subtract(faseAttiva.getTargetCaloricoAttuale()));
        } catch (FaseNonTrovataException e) {
            // Nessuna fase attiva: il riepilogo resta senza confronto col target.
        }

        return dto;
    }
}
