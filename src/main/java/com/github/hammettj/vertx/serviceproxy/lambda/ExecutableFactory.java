package com.github.hammettj.vertx.serviceproxy.lambda;

import com.github.hammettj.vertx.serviceproxy.Invocation;
import io.vertx.core.Future;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.invoke.MethodType.methodType;
import static java.util.Objects.requireNonNullElseGet;

public class ExecutableFactory {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final ConcurrentMap<Method, Executable<?, ?>> cache = new ConcurrentHashMap<>();

    public static <T> Executable<T, ?> makeExecutable(Invocation invocation) {
        final Invocation.Arguments args = invocation.args();

        return switch (args.length()) {
            case 0 -> create(Executable0.class, invocation.method()).asSpreader(args);
            case 1 -> create(Executable1.class, invocation.method()).asSpreader(args);
            case 2 -> create(Executable2.class, invocation.method()).asSpreader(args);
            case 3 -> create(Executable3.class, invocation.method()).asSpreader(args);
            case 4 -> create(Executable4.class, invocation.method()).asSpreader(args);
            case 5 -> create(Executable5.class, invocation.method()).asSpreader(args);
            case 6 -> create(Executable6.class, invocation.method()).asSpreader(args);
            case 7 -> create(Executable7.class, invocation.method()).asSpreader(args);
            case 8 -> create(Executable8.class, invocation.method()).asSpreader(args);
            case 9 -> create(Executable9.class, invocation.method()).asSpreader(args);
            case 10 -> create(Executable10.class, invocation.method()).asSpreader(args);
            default -> throw new IllegalStateException("Unexpected number of arguments: " + args.length());
        };
    }

    private static <E extends Executable<?, ?>> E create(Class<E> intfc, Method target) {
        return intfc.cast(requireNonNullElseGet(cache.get(target),
            () -> cache.computeIfAbsent(target, method -> {
                try {
                    final MethodHandle targetMH = lookup.unreflect(target);
                    final CallSite callSite = LambdaMetafactory.metafactory(lookup,
                        "apply",
                        methodType(intfc),
                        targetMH.type().generic().changeReturnType(Future.class),
                        targetMH,
                        targetMH.type().wrap()
                    );

                    return intfc.cast(callSite.getTarget().invoke());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            })
        ));
    }

    public interface Executable<T, RTYPE> {
        Future<RTYPE> apply(T service);
    }

    private interface Executable0<T, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return this;
        }
    }

    private interface Executable1<T, A, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0));
        }
    }

    private interface Executable2<T, A, B, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1));
        }
    }

    private interface Executable3<T, A, B, C, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2));
        }
    }

    private interface Executable4<T, A, B, C, D, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3));
        }
    }

    private interface Executable5<T, A, B, C, D, E, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4));
        }
    }

    private interface Executable6<T, A, B, C, D, E, F, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e, F f);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4), args.arg(5));
        }
    }

    private interface Executable7<T, A, B, C, D, E, F, G, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e, F f, G g);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4), args.arg(5), args.arg(6));
        }
    }

    private interface Executable8<T, A, B, C, D, E, F, G, H, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e, F f, G g, H h);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4), args.arg(5), args.arg(6), args.arg(7));
        }
    }

    private interface Executable9<T, A, B, C, D, E, F, G, H, I, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e, F f, G g, H h, I i);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4), args.arg(5), args.arg(6), args.arg(7), args.arg(8));
        }
    }

    private interface Executable10<T, A, B, C, D, E, F, G, H, I, J, RTYPE> extends Executable<T, RTYPE> {
        Future<RTYPE> apply(T service, A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);

        default Executable<T, RTYPE> asSpreader(Invocation.Arguments args) {
            return service -> apply(service, args.arg(0), args.arg(1), args.arg(2), args.arg(3), args.arg(4), args.arg(5), args.arg(6), args.arg(7), args.arg(8), args.arg(9));
        }
    }
}
