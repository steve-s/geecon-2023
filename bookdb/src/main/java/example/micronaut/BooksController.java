package example.micronaut;

import example.micronaut.domain.Book;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.stream.Stream;

@ExecuteOn(TaskExecutors.IO)
@Controller("/books")
public class BooksController {
    private final BooksRepository booksRepository;
    private final Context context;

    BooksController(BooksRepository booksRepository, Context context) {
        this.booksRepository = booksRepository;
        this.context = context;
    }

    @Get(value = "/list{?args*}")
    Stream<Book> list(@Valid FilterArguments args) {
        Stream<Book> result = booksRepository.findAll();
        if (args.pyFilter() != null) {
            System.out.println("Going to execute: ");
            System.out.println(args.pyFilter());
            return applyPyFilter(args, result);
        }
        return result;
    }

    private Stream<Book> applyPyFilter(FilterArguments args, Stream<Book> result) {
        Value module = context.eval("python", args.pyFilter());
        Value filter = module.getMember("filter");
        if (filter != null) {
            result = result.filter(x -> filter.execute(x).asBoolean());
        }
        return result;
    }
}
