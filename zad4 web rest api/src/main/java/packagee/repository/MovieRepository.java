package packagee.repository;

import java.util.List;

import javax.transaction.Transactional;

import packagee.entity.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MovieRepository extends JpaRepository<Movie, Integer> {
  List<Movie> findByTitleContainingIgnoreCase(String keyword, Sort sort);
}