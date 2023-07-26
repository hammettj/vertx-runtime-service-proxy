package com.github.hammettj.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(ConsumerVerticle.class, new DeploymentOptions())
            .compose(ignored -> vertx.deployVerticle(ProducerVerticle.class, new DeploymentOptions().setInstances(1)))
            .onFailure(ex -> {
                ex.printStackTrace();

                vertx.close();
            })
            .onSuccess(deploymentId -> vertx.close());
    }
}
