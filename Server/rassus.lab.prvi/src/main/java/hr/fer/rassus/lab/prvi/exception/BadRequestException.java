package hr.fer.rassus.lab.prvi.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RassusPrviLabException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
