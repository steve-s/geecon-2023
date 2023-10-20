Example that shows how GraalPy can be used in an Micronaut application to
deliver scripting capabilities. It shows a table with books that can be
filtered using a Python script that the user can provide in the UI.

Run with:

```bash
mvn mn:run
```

Disclaimer: this is an example that omits otherwise important details such as
validation, error handling, and most importantly security considerations.
The Polyglot provides options to harden the isolation between the GraalPy
execution context and the host, but does not provide a secure sandbox.

Oracle GraalVM provides [Polyglot Sandboxing](https://www.graalvm.org/latest/security-guide/polyglot-sandbox/),
but it is available only for JavaScript for now.

## Micronaut 4.1.2 Documentation

- [User Guide](https://docs.micronaut.io/4.1.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.1.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.1.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---
