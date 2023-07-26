package com.github.hammettj.vertx;

import com.github.hammettj.vertx.serviceproxy.ServiceProxyProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ProducerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        final ServiceProxyProvider serviceProxyProvider = ServiceProxyProvider.getDefault(vertx);

        serviceProxyProvider.createProxy(HelloService.class, HelloService.ADDRESS)
            .safeToUpperCase("hello world")
            .onSuccess(upper -> System.out.printf("<<<<< %s%n", upper))
            .<Void>mapEmpty()
            .onComplete(startPromise);
    }
}
