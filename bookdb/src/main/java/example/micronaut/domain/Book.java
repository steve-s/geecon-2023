package example.micronaut.domain;

import example.micronaut.ExposeInPython;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.graalvm.polyglot.HostAccess;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @HostAccess.Export
    public String name;

    @NotNull
    @Column(name = "author", nullable = false)
    @HostAccess.Export
    public String author;

    @NotNull
    @Column(name = "isbn", nullable = false)
    @HostAccess.Export
    public String isbn;

    @NotNull
    @Column(name = "genre", nullable = false)
    @HostAccess.Export
    public String genre;

    @HostAccess.Export
    public int ranking;

    public Book() {}

    public Book(@NotNull String isbn,
                @NotNull String name,
                @NotNull String author,
                @NotNull String genre,
                int ranking) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre=" + genre +
                '}';
    }
}
