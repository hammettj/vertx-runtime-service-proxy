package com.github.hammettj.vertx.serviceproxy;

import io.vertx.core.Vertx;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.requireNonNull;
import static net.bytebuddy.matcher.ElementMatchers.isAbstract;

public interface ServiceProxyProvider {

    <T> T createProxy(Class<T> clazz, String address);

    static ServiceProxyProvider getDefault(Vertx vertx) {
        requireNonNull(vertx);

        return new ServiceProxyProvider() {
            private static final ConcurrentMap<Class<?>, Constructor<?>> cache = new ConcurrentHashMap<>();

            @SuppressWarnings("unchecked")
            @Override
            public <T> T createProxy(Class<T> clazz, String address) {
                try {
                    Constructor<?> constructor = cache.get(clazz);

                    if (constructor == null) {
                        constructor = cache.computeIfAbsent(clazz, c -> {
                            if (!clazz.isInterface()) {
                                throw new IllegalArgumentException(clazz.getCanonicalName() + " is not an interface");
                            }
                            // todo: maybe we should check that return type of every abstract method is io.vertx.core.Future

                            try {
                                return generateProxyClass(clazz, new VertxEBClientProxyDelegate(vertx, requireNonNull(address)))
                                    .getConstructor();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    return (T) constructor.newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }

            @SuppressWarnings("resource")
            private <T> Class<? extends T> generateProxyClass(Class<T> intf, VertxEBClientProxyDelegate delegate) {
                return new ByteBuddy()
                    .subclass(intf)
                    .method(isAbstract())
                    .intercept(MethodDelegation.to(delegate))
                    .make()
                    .load(intf.getClassLoader())
                    .getLoaded();
            }
        };
    }
}
