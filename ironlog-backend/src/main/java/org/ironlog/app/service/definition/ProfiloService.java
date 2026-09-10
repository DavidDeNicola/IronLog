package org.ironlog.app.service.definition;

import org.ironlog.app.dto.CambioPasswordRequestDTO;
import org.ironlog.app.model.Utente;

public interface ProfiloService {

    void cambiaPassword(Utente utente, CambioPasswordRequestDTO request);

}