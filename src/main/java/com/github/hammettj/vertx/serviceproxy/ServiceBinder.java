package com.github.hammettj.vertx.serviceproxy;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;

import static com.github.hammettj.vertx.serviceproxy.ProxyHandler.newProxyHandler;
import static java.util.Objects.requireNonNull;

public class ServiceBinder {

    private final Vertx vertx;
    private String address;

    public ServiceBinder(Vertx vertx) {
        this.vertx = requireNonNull(vertx);
    }

    public ServiceBinder setAddress(String address) {
        this.address = address;
        return this;
    }

    public <T> MessageConsumer<Invocation> register(Class<T> clazz, T service) {
        requireNonNull(address);

        final var proxyHandler = newProxyHandler(clazz, service);
        return vertx.eventBus().consumer(address, proxyHandler);
    }
}
