package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RisultatoImportDTO {

    private int righeLette;
    private int alimentiCreati;
    private int alimentiAggiornati;
    private int righeScartate;
    private List<String> errori = new ArrayList<>();
}
