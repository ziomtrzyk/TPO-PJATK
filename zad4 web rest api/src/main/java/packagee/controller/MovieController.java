package packagee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import packagee.entity.Movie;
import packagee.repository.MovieRepository;

@Controller
public class MovieController {

  @Autowired
  private MovieRepository movieRepository;

  @GetMapping("/movies")
  public String getAll(Model model, @RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "id,asc") String[] sort) {
    try {
      List<Movie> movies = new ArrayList<Movie>();

      String sortField = sort[0];
      String sortDirection = sort[1];

      Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
      Order order = new Order(direction, sortField);

      if (keyword == null) {
        movies = movieRepository.findAll(Sort.by(order));
      } else {
        movies = movieRepository.findByTitleContainingIgnoreCase(keyword, Sort.by(order));
        model.addAttribute("keyword", keyword);
      }

      model.addAttribute("movies", movies);
      model.addAttribute("sortField", sortField);
      model.addAttribute("sortDirection", sortDirection);
      model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "movies";
  }

  @GetMapping("/movies/new")
  public String addMovie(Model model) {
    Movie movie = new Movie();

    model.addAttribute("movie", movie);
    model.addAttribute("pageTitle", "Create new Movie");

    return "movie_form";
  }

  @PostMapping("/movies/save")
  public String saveMovie(Movie movie, RedirectAttributes redirectAttributes) {
    try {
      movieRepository.save(movie);

    } catch (Exception e) {
      redirectAttributes.addAttribute("message", e.getMessage());
    }

    return "redirect:/movies";
  }

  @GetMapping("/movies/{id}")
  public String editMovie(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Movie movie = movieRepository.findById(id).get();

      model.addAttribute("movie", movie);
      model.addAttribute("pageTitle", "Edit Movie (ID: " + id + ")");

      return "movie_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/movies";
    }
  }

  @GetMapping("/movies/delete/{id}")
  public String deleteMovie(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      movieRepository.deleteById(id);

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/movies";
  }
}