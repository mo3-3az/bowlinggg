package com.freeletics.bowlinggg.verticle.server;

import com.freeletics.bowlinggg.VertxBasedTest;
import com.freeletics.bowlinggg.config.Addresses;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.*;

/**
 * @author Moath
 */
@RunWith(VertxUnitRunner.class)
public class WebServerTest extends VertxBasedTest {

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        start(testContext);
    }

    @Test
    public void testWebServer(TestContext testContext) {
        Async async1 = testContext.async();
        Async async2 = testContext.async();
        Async async3 = testContext.async();
        Async async4 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test("startNewGame", event -> {
                    final ExtractableResponse<Response> response = post(Addresses.GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    testContext.assertEquals("", response.asString());
                    async1.complete();
                })
                .test("updateGame", event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put("/v1/games/1/1")
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals("", response.asString());
                    async2.complete();
                })
                .test("getGame", event -> {
                    async2.awaitSuccess();
                    final ExtractableResponse<Response> response = get("/v1/games/1")
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_OK, response.statusCode());
                    testContext.assertEquals(1, new JsonObject(response.asString()).getJsonObject("game").getInteger("score"));
                    async3.complete();
                })
                .test("deleteGame", event -> {
                    async3.awaitSuccess();
                    final ExtractableResponse<Response> response = delete("/v1/games/1")
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals("", response.asString());
                    async4.complete();
                })
                .run(vertx, new TestOptions());

        async4.awaitSuccess();
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stop(testContext);
    }
}
