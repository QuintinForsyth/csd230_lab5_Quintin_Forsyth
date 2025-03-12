package csd230.lab2.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class DiscMagNotFoundAdvice {

    @ExceptionHandler(DiscMagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String discMagNotFoundHandler(DiscMagNotFoundException ex) {
        return ex.getMessage();
    }
}
