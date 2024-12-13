package hr.fer.rassus.lab.prvi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RassusPrviLabException extends RuntimeException {
    private final HttpStatus httpStatus;

    public RassusPrviLabException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
