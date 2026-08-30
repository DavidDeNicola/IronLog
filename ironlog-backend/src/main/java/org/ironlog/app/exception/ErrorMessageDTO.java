package org.ironlog.app.exception;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ErrorMessageDTO {

    private String messaggio;
    private LocalDateTime timestamp;

    public ErrorMessageDTO(String messaggio) {
        this.messaggio = messaggio;
        this.timestamp = LocalDateTime.now();
    }

}
