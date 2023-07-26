package com.github.hammettj.vertx;

import io.vertx.core.Future;

import static io.vertx.core.Future.succeededFuture;
import static java.util.Objects.requireNonNullElse;

public class HelloServiceImpl implements HelloService {

    @Override
    public Future<String> safeToUpperCase(String str) {
        return succeededFuture(requireNonNullElse(str, "").toUpperCase());
    }
}
