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
    private static final String GAME_NOT_FOUND_TEST_CASE = "gameNotFound";

    private static final String JSON_KEY_GAME = Game.JSON_KEY_GAME;
    private static final String JSON_KEY_ID = Game.JSON_KEY_ID;
    private static final String JSON_KEY_SCORE = Game.JSON_KEY_SCORE;
    private static final String JSON_KEY_ERROR = WebServer.JSON_KEY_ERROR;
    private static final String ASSERT_ERROR_MSG_ERROR_SHOULD_BE_TRUE = "Error should be true!";


    private static final String EXPECTED_EMPTY = "";
    private static final String STRIKE = "10";
    private static final String NONEXISTENT_GAME_ID = "1000";
    private static final String INVALID_GAME_ID = "X";
    private static final String INVALID_PINS_VALUE = "X";

    private static final int REPEAT = 12;
    private static final int PERFECT_GAME_SCORE = 300;
    private static final int ONE_PIN = 1;
    private static final String INVALID_PINS_VALUE_TEST_CASE = "invalidPinsValue";
    private static final String INVALID_GAME_ID_TEST_CASE = "invalidGameID";

    private int count;
    private String firstGameId;
    private String secondGameId;
    private String thirdGameId;

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
                    final ExtractableResponse<Response> response = post(GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    firstGameId = new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getString(JSON_KEY_ID);
                    async1.complete();
                })
                .test(UPDATE_GAME_TEST_CASE, event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put(GAMES_ENDPOINT + firstGameId + "/" + ONE_PIN)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
                    async2.complete();
                })
                .test(GET_GAME_TEST_CASE, event -> {
                    async2.awaitSuccess();
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + firstGameId)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_OK, response.statusCode());
                    testContext.assertEquals(ONE_PIN, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getInteger(JSON_KEY_SCORE));
                    async3.complete();
                })
                .test(DELETE_GAME_TEST_CASE, event -> {
                    async3.awaitSuccess();
                    final ExtractableResponse<Response> response = delete(GAMES_ENDPOINT + firstGameId)
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
                    final ExtractableResponse<Response> response = post(GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    secondGameId = new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getString(JSON_KEY_ID);

                    async1.complete();
                })
                .test(UPDATE_GAME_TEST_CASE, REPEAT, event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put(GAMES_ENDPOINT + secondGameId + "/" + STRIKE)
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
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + secondGameId)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_OK, response.statusCode());
                    testContext.assertEquals(PERFECT_GAME_SCORE, new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getInteger(JSON_KEY_SCORE));
                    async3.complete();
                })
                .test(DELETE_GAME_TEST_CASE, event -> {
                    async3.awaitSuccess();
                    final ExtractableResponse<Response> response = delete(GAMES_ENDPOINT + secondGameId)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_NO_CONTENT, response.statusCode());
                    testContext.assertEquals(EXPECTED_EMPTY, response.asString());
                    async4.complete();
                })
                .run(vertx, new TestOptions());

        async4.awaitSuccess();
    }

    @Test
    public void testGameNotFound(TestContext testContext) {
        Async async1 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test(GAME_NOT_FOUND_TEST_CASE, event -> {
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + NONEXISTENT_GAME_ID)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
                    testContext.assertTrue(new JsonObject(response.asString()).getBoolean(JSON_KEY_ERROR), ASSERT_ERROR_MSG_ERROR_SHOULD_BE_TRUE);
                    async1.complete();
                })
                .run(vertx, new TestOptions());

        async1.awaitSuccess();
    }

    @Test
    public void testInvalidGameID(TestContext testContext) {
        Async async1 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test(INVALID_GAME_ID_TEST_CASE, event -> {
                    final ExtractableResponse<Response> response = get(GAMES_ENDPOINT + INVALID_GAME_ID)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
                    testContext.assertTrue(new JsonObject(response.asString()).getBoolean(JSON_KEY_ERROR), ASSERT_ERROR_MSG_ERROR_SHOULD_BE_TRUE);
                    async1.complete();
                })
                .run(vertx, new TestOptions());

        async1.awaitSuccess();
    }

    @Test
    public void testInvalidPinsValue(TestContext testContext) {
        Async async1 = testContext.async();
        Async async2 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test(START_NEW_GAME_TEST_CASE, event -> {
                    final ExtractableResponse<Response> response = post(GAMES_ENDPOINT)
                            .then()
                            .extract();
                    testContext.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
                    thirdGameId = new JsonObject(response.asString()).getJsonObject(JSON_KEY_GAME).getString(JSON_KEY_ID);

                    async1.complete();
                })
                .test(INVALID_PINS_VALUE_TEST_CASE, REPEAT, event -> {
                    async1.awaitSuccess();
                    final ExtractableResponse<Response> response = put(GAMES_ENDPOINT + thirdGameId + "/" + INVALID_PINS_VALUE)
                            .then().extract();
                    testContext.assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
                    testContext.assertTrue(new JsonObject(response.asString()).getBoolean(JSON_KEY_ERROR), ASSERT_ERROR_MSG_ERROR_SHOULD_BE_TRUE);
                    async2.complete();
                })
                .run(vertx, new TestOptions());

        async2.awaitSuccess();
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stop(testContext);
    }
}
