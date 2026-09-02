package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class SessioneRequestDTO {

    private Long giornoSchedaId;
    private String note;
}
