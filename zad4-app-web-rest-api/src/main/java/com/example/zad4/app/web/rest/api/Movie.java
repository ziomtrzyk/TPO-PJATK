package com.example.zad4.App.Web.REST.API;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Movie {

    private @Id @GeneratedValue Long id;
    private String title;
    private String director;

    public Movie() {}

    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Movie))
            return false;
        Movie movie = (Movie) o;
        return Objects.equals(this.id, movie.id) && Objects.equals(this.title, movie.title)
                && Objects.equals(this.director, movie.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.director);
    }

}
