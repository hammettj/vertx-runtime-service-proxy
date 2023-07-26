package com.github.hammettj.vertx;

import io.vertx.core.Future;

public interface HelloService {
    String ADDRESS = "service.hello";

    Future<String> safeToUpperCase(String str);

}
