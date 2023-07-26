package com.github.hammettj.vertx.serviceproxy;

import com.github.hammettj.vertx.serviceproxy.lambda.ExecutableFactory;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.hammettj.vertx.serviceproxy.lambda.ExecutableFactory.makeExecutable;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Comparator.comparing;

public class ProxyHandler<T> implements Handler<Message<Invocation>> {


    private final T service;
    private final Set<Method> methods;

    public ProxyHandler(T service, Set<Method> methods) {
        this.service = service;
        this.methods = methods;
    }

    public static <T> ProxyHandler<T> newProxyHandler(Class<T> intfc, T service) {
        final Set<Method> methods = Arrays.stream(intfc.getDeclaredMethods())
            .filter(m -> isAbstract(m.getModifiers()))
            .sorted(comparing(Method::getName))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return new ProxyHandler<>(service, methods);
    }

    @Override
    public void handle(Message<Invocation> msg) {
        final Invocation invocation = msg.body();

        final Method method = invocation.method();
        if (!methods.contains(method)) {
            throw new IllegalStateException("Invalid action: " + method.getName());
        }

        final ExecutableFactory.Executable<T, ?> executable = makeExecutable(invocation);
        executable.apply(service)
            .onFailure(ex -> msg.reply(new ReplyException(ReplyFailure.RECIPIENT_FAILURE, -1, ex.getMessage())))
            .onSuccess(res -> {
                if (res instanceof Enum<?> enumeration) {
                    msg.reply(enumeration.name());
                } else
                    msg.reply(res);
                });
    }
}
