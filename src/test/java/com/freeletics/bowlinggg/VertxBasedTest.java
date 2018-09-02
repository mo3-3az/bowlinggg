package com.freeletics.bowlinggg;

import com.freeletics.bowlinggg.config.Config;
import com.freeletics.bowlinggg.verticle.Deployer;
import com.jayway.restassured.RestAssured;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import java.io.IOException;
import java.net.ServerSocket;

public class VertxBasedTest {

    private static final String LOCALHOST = "http://localhost";
    private static final String VERTX_CACHE_DIR = ".vertx";

    protected static Vertx vertx;

    protected static void start(TestContext testContext) throws IOException {
        vertx = Vertx.vertx();
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        vertx.deployVerticle(new Deployer(new JsonObject().put(Config.HTTP_PORT, port)), testContext.asyncAssertSuccess());

        RestAssured.baseURI = LOCALHOST;
        RestAssured.port = port;
        testContext.async().complete();
    }

    protected static void stop(TestContext testContext) {
        RestAssured.reset();
        testContext.async().complete();
        vertx.fileSystem().deleteRecursiveBlocking(VERTX_CACHE_DIR, true);
        vertx.close();
    }

    protected EventBus eventBus() {
        return vertx.eventBus();
    }
}