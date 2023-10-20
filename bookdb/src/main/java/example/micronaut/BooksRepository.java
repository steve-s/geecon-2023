package example.micronaut;

import example.micronaut.domain.Book;

import java.util.List;
import java.util.stream.Stream;

public interface BooksRepository {
    Stream<Book> findAll();
}
