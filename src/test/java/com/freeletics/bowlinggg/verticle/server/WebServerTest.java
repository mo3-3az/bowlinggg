package com.freeletics.bowlinggg.verticle.server;

import com.freeletics.bowlinggg.VertxBasedTest;
import com.freeletics.bowlinggg.verticle.game.model.Game;
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

    private static final String GAMES_ENDPOINT = WebServer.GAMES_ENDPOINT + "/";

    private static final String START_NEW_GAME_TEST_CASE = "startNewGame";
    private static final String UPDATE_GAME_TEST_CASE = "updateGame";
    private static final String GET_GAME_TEST_CASE = "getGame";
    private static final String DELETE_GAME_TEST_CASE = "deleteGame";

    private static final String JSON_KEY_GAME = Game.JSON_KEY_GAME;
    private static final String JSON_KEY_ID = Game.JSON_KEY_ID;
    private static final String JSON_KEY_SCORE = Game.JSON_KEY_SCORE;

    private static final String EXPECTED_EMPTY = "";
    private static final String STRIKE = "10";
    private static final String FIRST_GAME_ID = "1";
    private static final String SECOND_GAME_ID = "2";

    private static final int REPEAT = 12;
    private static final int PERFECT_GAME_SCORE = 300;
    private static final int ONE_PIN = 1;

    private int count;

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
                .test(START_NEW_GAME_TEST_CASE, event -> {
                    final ExtractableResponse<Response> response = post(WebServer.GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    testContext.assertEquals(FIRST_GAME_ID, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getString(JSON_KEY_ID));
                    async1.complete();
                })
                .test(UPDATE_GAME_TEST_CASE, event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put(GAMES_ENDPOINT + FIRST_GAME_ID + "/" + ONE_PIN)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
                    async2.complete();
                })
                .test(GET_GAME_TEST_CASE, event -> {
                    async2.awaitSuccess();
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + FIRST_GAME_ID)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_OK, response.statusCode());
                    testContext.assertEquals(ONE_PIN, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getInteger(JSON_KEY_SCORE));
                    async3.complete();
                })
                .test(DELETE_GAME_TEST_CASE, event -> {
                    async3.awaitSuccess();
                    final ExtractableResponse<Response> response = delete(GAMES_ENDPOINT + FIRST_GAME_ID)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
                    async4.complete();
                })
                .run(vertx, new TestOptions());

        async4.awaitSuccess();
    }

    @Test
    public void perfectGame(TestContext testContext) {
        Async async1 = testContext.async();
        Async async2 = testContext.async();
        Async async3 = testContext.async();
        Async async4 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test(START_NEW_GAME_TEST_CASE, event -> {
                    final ExtractableResponse<Response> response = post(WebServer.GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    testContext.assertEquals(SECOND_GAME_ID, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getString(JSON_KEY_ID));

                    async1.complete();
                })
                .test(UPDATE_GAME_TEST_CASE, REPEAT, event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put(GAMES_ENDPOINT + SECOND_GAME_ID + "/" + STRIKE)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
                    count++;
                    if (count == REPEAT) {
                        async2.complete();
                    }
                })
                .test(GET_GAME_TEST_CASE, event -> {
                    async2.awaitSuccess();
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + SECOND_GAME_ID)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_OK, response.statusCode());
                    testContext.assertEquals(PERFECT_GAME_SCORE, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getInteger(JSON_KEY_SCORE));
                    async3.complete();
                })
                .test(DELETE_GAME_TEST_CASE, event -> {
                    async3.awaitSuccess();
                    final ExtractableResponse<Response> response = delete(GAMES_ENDPOINT + SECOND_GAME_ID)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
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
