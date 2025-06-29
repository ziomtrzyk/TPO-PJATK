package packagee.entity;

import javax.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(length = 128, nullable = false)
  private String title;

  @Column(length = 256)
  private String description;

  public Movie() {

  }

  public Movie(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Movie [id=" + id + ", title=" + title + ", description=" + description + "]";
  }
}