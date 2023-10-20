package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record FilterArguments(
         @Nullable
         String pyFilter
) {
}
