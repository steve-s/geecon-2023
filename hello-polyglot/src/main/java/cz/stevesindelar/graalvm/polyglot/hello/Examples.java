package cz.stevesindelar.graalvm.polyglot.hello;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;

import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Random;

public class Examples {

    public static void randomArray() {
        final int N = 1000000;
        int[] data = new int[N];
        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = r.nextInt();
        }
    }




    public static void sum() {
        try (Context ctx = Context.newBuilder()
                .allowHostAccess(HostAccess.ALL)
                .option("engine.TraceCompilation", "true")
                .build()) {
            Value module = ctx.eval("python", """
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
            Value pySum = module.getMember("sum");
            for (int i = 0; i < 10_000; i++) {
                long start = System.nanoTime();
                System.out.println(pySum.execute(data, 0, data.length));
                long end = System.nanoTime();
                System.out.printf("%.2f\n", (end - start) / 1_000_000.0);
            }
        }
    }

    public static void isolation() {
        try (Context ctx1 = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {

            try (Context ctx2 = Context.newBuilder()
                    .allowAllAccess(true)
                    .build()) {

                ctx1.eval("python", "x = 42");
                ctx1.eval("python", "print(x)");
                ctx2.eval("python", "print(x)");
            }
        }
    }

    public static class MyClass {
        public Object getResult() {
            return new Thread();
        }
    }

    public static void usingJavaClass() {
        try (Context ctx = Context.newBuilder()
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            Value module = ctx.eval("python", """
                    def foo(obj):
                        x = obj.getResult()
                        return x
                    """);
            System.out.println(module.getMember("foo").execute(new MyClass()).asHostObject() instanceof Thread);
        }
    }

    public static void usingPyClass() {
        try (Context ctx = Context.newBuilder()
                .allowAllAccess(true)
                .out(OutputStream.nullOutputStream())
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            Value module = ctx.eval("python", """
                    class PyClass:
                        def __init__(self, x):
                            self.x = x
                    
                        def bar(self, val):
                            print('We are really in Python')
                            return self.x + val
                    """);
            Value pyClass = module.getMember("PyClass");
            Value instance = pyClass.newInstance(39);
            Value result = instance.invokeMember("bar", 3);
            System.out.println(result);
        }
    }

    public interface BarService {
        int bar(int val);
    }

    public static void usingPyClassAsIface(String[] args) {
        try (Context ctx = Context.newBuilder()
                .allowAllAccess(true)
                .out(OutputStream.nullOutputStream())
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            Value module = ctx.eval("python", """
                    class PyClass:
                        def __init__(self, x):
                            self.x = x
                    
                        def bar(self, val):
                            print('We are really in Python')
                            return self.x + val
                    """);
            Value pyClass = module.getMember("PyClass");
            BarService barService = pyClass.newInstance(39).as(BarService.class);
            int result = barService.bar(3);
            System.out.println(result);
        }
    }

    public static void restrictions(String[] args) {
        try (Context ctx = Context.newBuilder()
                .out(OutputStream.nullOutputStream())
                .allowIO(IOAccess.ALL)
                .currentWorkingDirectory(Paths.get("/something_that_doesnt_exist"))
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            Value module = ctx.eval("python", """
                    class PyClass:
                        def __init__(self, x):
                            self.x = x
                    
                        def bar(self, val):
                            print('We are really in Python')
                            return self.x + val
                    """);
            Value pyClass = module.getMember("PyClass");
            Value instance = pyClass.newInstance(39);
            Value result = instance.invokeMember("bar", 3);
            System.out.println(result);

            System.out.println(ctx.eval("python", "import os; os.getcwd()"));
        }
    }
}