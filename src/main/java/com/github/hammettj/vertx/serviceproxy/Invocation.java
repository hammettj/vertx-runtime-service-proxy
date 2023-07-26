package com.github.hammettj.vertx.serviceproxy;

import java.lang.reflect.Method;

public record Invocation(Method method, Arguments args) {

    public Invocation(Method method, Object... args) {
        this(method, new Arguments(args));
    }

    public record Arguments(Object... args) {
        public int length() {
            return args.length;
        }

        @SuppressWarnings("unchecked")
        public <T> T arg(int index) {
            if (args.length == 0 || index >= args.length) {
                throw new IndexOutOfBoundsException(index);
            }

            return (T) args[index >= 0 ? index : args.length - 1];
        }
    }

}
