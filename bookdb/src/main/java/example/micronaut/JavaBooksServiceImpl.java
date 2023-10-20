package example.micronaut;

import example.micronaut.domain.Book;
import io.micronaut.context.annotation.Bean;
import org.graalvm.polyglot.HostAccess;

@Bean
public class JavaBooksServiceImpl implements JavaBooksService {
    @Override
    @HostAccess.Export
    public boolean isGoodBook(Book b) {
        // Uncomment to demonstrate that Python passes the original
        // Java object to the Java service and not some
        // serialized/deserialized version
        // b.name += " I am doing evil things";
        return b.getName().contains("Java");
    }
}
