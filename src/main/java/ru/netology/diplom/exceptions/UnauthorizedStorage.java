package ru.netology.diplom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedStorage extends RuntimeException {
    public UnauthorizedStorage(String message) {
        super(message);
    }
}
