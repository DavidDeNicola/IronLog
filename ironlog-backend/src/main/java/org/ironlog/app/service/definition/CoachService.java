package org.ironlog.app.service.definition;

import org.ironlog.app.dto.AssunzioneIntegratoreResponseDTO;
import org.ironlog.app.dto.ConsumoAcquaResponseDTO;
import org.ironlog.app.dto.FaseRequestDTO;
import org.ironlog.app.dto.FaseResponseDTO;
import org.ironlog.app.dto.FaseSintesiDTO;
import org.ironlog.app.dto.MisurazioneCorporeaRequestDTO;
import org.ironlog.app.dto.MisurazioneCorporeaResponseDTO;
import org.ironlog.app.dto.PianoAlimentareResponseDTO;
import org.ironlog.app.dto.PianoAlimentareSintesiDTO;
import org.ironlog.app.dto.ProfiloMetabolicoRequestDTO;
import org.ironlog.app.dto.ProfiloMetabolicoResponseDTO;
import org.ironlog.app.dto.RiepilogoDiarioDTO;
import org.ironlog.app.dto.SchedaRequestDTO;
import org.ironlog.app.dto.SchedaResponseDTO;
import org.ironlog.app.dto.SchedaSintesiDTO;
import org.ironlog.app.dto.StatisticaClienteDTO;
import org.ironlog.app.dto.UtenteResponseDTO;
import org.ironlog.app.dto.VoceDiarioResponseDTO;
import org.ironlog.app.model.Utente;

import java.time.LocalDate;
import java.util.List;

public interface CoachService {

    List<UtenteResponseDTO> findAtleti(Utente coach);

    SchedaResponseDTO createSchedaPerAtleta(SchedaRequestDTO dto, Long atletaId, Utente coach);

    List<SchedaSintesiDTO> findSchedeAtleta(Long atletaId, Utente coach);

    List<StatisticaClienteDTO> statisticheClienti(Utente coach);

    MisurazioneCorporeaResponseDTO createMisurazionePerAtleta(MisurazioneCorporeaRequestDTO dto, Long atletaId, Utente coach);

    List<MisurazioneCorporeaResponseDTO> findMisurazioniAtleta(Long atletaId, Utente coach);

    ProfiloMetabolicoResponseDTO calcolaProfiloMetabolicoPerAtleta(ProfiloMetabolicoRequestDTO dto, Long atletaId, Utente coach);

    ProfiloMetabolicoResponseDTO findProfiloMetabolicoAttualeAtleta(Long atletaId, Utente coach);

    FaseResponseDTO createFasePerAtleta(FaseRequestDTO dto, Long atletaId, Utente coach);

    FaseResponseDTO findFaseAttivaAtleta(Long atletaId, Utente coach);

    List<FaseSintesiDTO> findFasiAtleta(Long atletaId, Utente coach);

    FaseResponseDTO chiudiFasePerAtleta(Long faseId, Long atletaId, Utente coach);

    List<VoceDiarioResponseDTO> findDiarioAtleta(Long atletaId, LocalDate data, Utente coach);

    RiepilogoDiarioDTO riepilogoDiarioAtleta(Long atletaId, LocalDate data, Utente coach);

    List<ConsumoAcquaResponseDTO> findAcquaAtleta(Long atletaId, LocalDate data, Utente coach);

    List<AssunzioneIntegratoreResponseDTO> findAssunzioniIntegratoriAtleta(Long atletaId, LocalDate data, Utente coach);

    PianoAlimentareResponseDTO generaPianoAlimentarePerAtleta(Long faseId, Long atletaId, Utente coach);

    List<PianoAlimentareSintesiDTO> findPianiAlimentariAtleta(Long faseId, Long atletaId, Utente coach);
}