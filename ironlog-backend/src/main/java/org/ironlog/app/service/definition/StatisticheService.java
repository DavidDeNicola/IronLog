package org.ironlog.app.service.definition;

import org.ironlog.app.dto.VolumeGruppoDTO;
import org.ironlog.app.model.PeriodoStatistica;
import org.ironlog.app.model.Utente;

import java.util.List;

public interface StatisticheService {

    List<VolumeGruppoDTO> volumePerGruppo(Utente atleta, PeriodoStatistica periodo);
}
