package tn.esprit.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Traduit les erreurs de validation (dont TarificationInconnueException)
 * en réponse HTTP 400 lisible au lieu d'une erreur 500.
 */
@RestControllerAdvice
public class GestionErreurs {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> requeteInvalide(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erreur", e.getMessage()));
    }
}
