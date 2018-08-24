package com.freeletics.bowlinggg;

import com.freeletics.bowlinggg.config.Config;
import com.freeletics.bowlinggg.verticle.Deployer;
import com.jayway.restassured.RestAssured;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

import java.io.IOException;
import java.net.ServerSocket;

public class VertxBasedTest {

    private static final String LOCALHOST = "http://localhost/";

    protected static Vertx vertx = Vertx.vertx();

    protected static void startVertx(TestContext testContext) throws IOException {
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        vertx.deployVerticle(new Deployer(new JsonObject().put(Config.HTTP_PORT, port)), testContext.asyncAssertSuccess());

        RestAssured.baseURI = LOCALHOST;
        RestAssured.port = port;
    }

    protected static void stopVertx(TestContext testContext) {
        RestAssured.reset();
    }
}