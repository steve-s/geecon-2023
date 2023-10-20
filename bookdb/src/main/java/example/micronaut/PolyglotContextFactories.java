package example.micronaut;

import example.micronaut.domain.Book;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Factory
public class PolyglotContextFactories {
    @Singleton
    @Bean(preDestroy = "close")
    Engine createEngine() {
        return Engine.newBuilder().allowExperimentalOptions(true).build();
    }

    @Prototype
    @Bean(preDestroy = "close")
    Context createContext(Engine engine, JavaBooksService javaBooksService) {
        Context ctx = Context.newBuilder("python", "java")
                .allowHostAccess(HostAccess.EXPLICIT)
                .engine(engine)
                .build();
        ctx.getBindings("python").putMember("javaBooksService", javaBooksService);
        return ctx;
    }
}
