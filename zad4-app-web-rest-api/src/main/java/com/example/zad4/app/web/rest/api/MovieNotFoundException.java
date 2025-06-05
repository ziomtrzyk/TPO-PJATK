package com.example.zad4.App.Web.REST.API;

public class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException(Long id  ) {super("Could not find movie with id "+id);}
}
