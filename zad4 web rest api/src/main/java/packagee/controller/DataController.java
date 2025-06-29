package packagee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import packagee.entity.Movie;
import packagee.repository.MovieRepository;

import java.util.List;

@RestController
public class  DataController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/data")
    List<Movie> data() {
        return movieRepository.findAll();
    }

    @PostMapping("/data")
    Movie add(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @GetMapping("/data/{id}")
    Movie get(@PathVariable int id) {
        return movieRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Movie not found"));
    }

    @PutMapping("/data/{id}")
    Movie update(@PathVariable int id, @RequestBody Movie newMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setDescription(newMovie.getDescription());
                    return movieRepository.save(movie);
                })
                .orElseGet(() -> movieRepository.save(newMovie));
    }
    @DeleteMapping("/data/{id}")
    void delete(@PathVariable int id) {
        movieRepository.deleteById(id);
    }

    @GetMapping("/data/sorted")
    List<Movie> dataSorted() {
        List<Movie> movies = movieRepository.findAll();
        movies.sort((m1, m2) -> m1.getTitle().compareTo(m2.getTitle()));
        return movies;
    }
}
