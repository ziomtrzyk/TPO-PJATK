package com.example.zad4.App.Web.REST.API;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MovieNotFoundAdvice {

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String movieNotFoundHandler(MovieNotFoundException ex) {return ex.getMessage();}
}
