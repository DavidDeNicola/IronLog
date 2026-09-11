package org.ironlog.app.service.definition;

import org.ironlog.app.dto.RisultatoImportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportatoreAlimentiService {

    RisultatoImportDTO importa(MultipartFile file) throws IOException;
}
