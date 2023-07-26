package com.github.hammettj.vertx;

import com.github.hammettj.vertx.serviceproxy.ServiceBinder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ConsumerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        new ServiceBinder(vertx)
            .setAddress(HelloService.ADDRESS)
            .register(HelloService.class, new HelloServiceImpl());

        startPromise.complete();
    }
}
