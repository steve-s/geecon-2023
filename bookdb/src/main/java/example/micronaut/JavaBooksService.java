package example.micronaut;

import example.micronaut.domain.Book;
import org.graalvm.polyglot.HostAccess;

@HostAccess.Implementable
public interface JavaBooksService {
    @HostAccess.Export
    boolean isGoodBook(Book b);
}
