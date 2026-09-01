package org.ironlog.app.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessageDTO> tokenScaduto(ExpiredJwtException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Sessione scaduta, effettua di nuovo il login");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorMessageDTO> firmaNonValida(SignatureException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Token non valido");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorMessageDTO> tokenMalformato(MalformedJwtException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Token malformato");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> utenteNonTrovato(UsernameNotFoundException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Credenziali non valide");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> erroreGenerico(Exception e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Errore interno del server");
        log.error("Errore non gestito", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(CredenzialiNonValideException.class)
    public ResponseEntity<ErrorMessageDTO> credenzialiNonValide(CredenzialiNonValideException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Credenziali non valide");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(EmailGiaRegistrataException.class)
    public ResponseEntity<ErrorMessageDTO> emailGiaRegistrata(EmailGiaRegistrataException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Email già registrata");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessageDTO> noResourceFound(NoResourceFoundException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Risorsa non trovata");
        log.warn("Risorsa non trovata", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageDTO> httpMessageNotReadable(HttpMessageNotReadableException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Body della richiesta non valido");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> methodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Parametri non validi");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(SchedaNonTrovataException.class)
    public ResponseEntity<ErrorMessageDTO> schedaNonTrovata(SchedaNonTrovataException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Scheda non trovata");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EsercizioNonTrovatoException.class)
    public ResponseEntity<ErrorMessageDTO> esercizioNonTrovato(EsercizioNonTrovatoException e) {
        ErrorMessageDTO body = new ErrorMessageDTO("Esercizio non trovato");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
