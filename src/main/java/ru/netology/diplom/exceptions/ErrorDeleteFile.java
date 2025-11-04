package ru.netology.diplom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErrorDeleteFile extends RuntimeException {
    public ErrorDeleteFile(String message) {
        super(message);
    }
}
