package cz.stevesindelar.graalvm.polyglot.hello;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.util.Random;

public class Main {
    // Simple example of Polyglot embedding
    // Run the program on OpenJDK 21 and GraalVM for JDK21 to see how
    // the Python function runs faster on GraalVM thanks to JIT compilation.
    // See https://github.com/graalvm/polyglot-embedding-demo/ for
    // more examples and how to run on OpenJDK with JIT compilation enabled.
    // There are more examples in the "Examples" class
    public static void main(String[] args) {
        System.out.println("Hello world from Java");
        try (Context ctx = Context.newBuilder()
                // this option below is not supported on non GraalVM JDKs, e.g., OpenJDK
                // .option("engine.TraceCompilation", "true")
                .allowHostAccess(HostAccess.ALL).build()) {
            Value module = ctx.eval("python",
                    """
                           def sum(arr, start, end):
                                 result = 0
                                 for i in range(start, end):
                                     result += arr[i]
                                 return result
                           """);

            final int N = 1000000;
            int[] data = new int[N];
            Random r = new Random();
            for (int i = 0; i < data.length; i++) {
                data[i] = r.nextInt();
            }

            Value sum = module.getMember("sum");
            for (int i = 0; i < 100; i++) {
                long start = System.nanoTime();
                sum.execute(data, 0, data.length);
                long end = System.nanoTime();
                System.out.printf("%.2f\n", (end - start) / 1_000_000.0);
            }

            System.out.println(sum.execute(new int[]{1,2,3}, 0, 2));
        }
    }
}