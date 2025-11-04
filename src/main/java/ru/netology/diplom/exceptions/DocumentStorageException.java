package ru.netology.diplom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DocumentStorageException extends RuntimeException {
    public DocumentStorageException(String message) {
        super(message);
    }
}
