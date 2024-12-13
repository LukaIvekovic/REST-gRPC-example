package hr.fer.rassus.lab.prvi.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RassusPrviLabException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
