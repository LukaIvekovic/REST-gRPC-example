package hr.fer.rassus.lab.prvi.config;

import hr.fer.rassus.lab.prvi.exception.RassusPrviLabException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAnyException(Exception e) {
        log.error("An error occurred: ", e);

        return ResponseEntity.internalServerError().body("An unexcepted internal server error occured: " + e.getMessage());
    }

    @ExceptionHandler(RassusPrviLabException.class)
    public final ResponseEntity<String> handleAnyCustomException(RassusPrviLabException e) {
        log.error("Unhandled custom exception occured: ", e);

        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
