package com.example.zad4.App.Web.REST.API;

import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
public class MovieController {

    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/movies")
    List<Movie> getAllMovies() {
        return repository.findAll();
    }
    @GetMapping("/moviesorted")
    List<Movie> getMoviesSorted() {
        List<Movie> list = repository.findAll();
        list.sort(Comparator.comparing(Movie::getTitle));
        return list;
    }

    @GetMapping("/movies/{id}")
    Movie getMovieById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    @PostMapping("/movies")
    Movie addMovie(@RequestBody Movie movie) {
        return repository.save(movie);
    }

    @PutMapping("/movies/{id}")
    Movie updateMovie(@PathVariable Long id, @RequestBody Movie newMovie) {

        return repository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setDirector(newMovie.getDirector());
                    return repository.save(movie);
                })
                .orElseGet(() -> repository.save(newMovie));
    }

    @DeleteMapping("/movies/{id}")
    void deleteMovie(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
