package com.github.hammettj.vertx.serviceproxy;

import java.lang.reflect.Method;

import com.github.hammettj.vertx.serviceproxy.codec.InvocationMessageCodec;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import static java.util.Objects.requireNonNull;

public class VertxEBClientProxyDelegate {

    private final Vertx vertx;
    private final String address;

    public VertxEBClientProxyDelegate(Vertx vertx, String address) {
        this.vertx = requireNonNull(vertx);
        this.address = requireNonNull(address);

        try {
            this.vertx.eventBus().registerDefaultCodec(Invocation.class, InvocationMessageCodec.CODEC);
        } catch (IllegalStateException ex) {
        }
    }

    @SuppressWarnings("unused")
    @RuntimeType
    public Future<?> intercept(@Origin Method method, @AllArguments Object[] args) {
        return vertx.eventBus().request(address, new Invocation(method, args))
            .map(Message::body);
    }
}
