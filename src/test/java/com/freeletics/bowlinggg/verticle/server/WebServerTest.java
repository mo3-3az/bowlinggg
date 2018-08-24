package com.freeletics.bowlinggg.verticle.server;

import com.freeletics.bowlinggg.VertxBasedTest;
import com.freeletics.bowlinggg.config.Addresses;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.post;

/**
 * @author Moath
 */
@RunWith(VertxUnitRunner.class)
public class WebServerTest extends VertxBasedTest {

    private static final String FIRST_GAME_ID = "1";

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        startVertx(testContext);
    }

    @Test
    public void name() {
        post(Addresses.GAMES_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stopVertx(testContext);
    }

    //    @BeforeClass
//    public static void setUp() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = Integer.getInteger("http.port", 8080);
//    }
//
//    @Test
//    public void startNewGame() {
//        post(Addresses.GAMES_ENDPOINT)
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.SC_CREATED)
//                .body(getGame().toJsonString(), equalTo(""));
//    }
//
//    @Test
//    public void getGameById() {
//        get(Addresses.GAMES_ENDPOINT + "/{id}", FIRST_GAME_ID)
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.SC_OK)
//                .body(getGame().toJsonString(), equalTo(""));
//    }
//
//    @Test
//    public void updateGame() {
//        put(Addresses.GAMES_ENDPOINT + "/{id}", FIRST_GAME_ID)
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.SC_NO_CONTENT)
//                .body(getGame().toJsonString(), equalTo(""));
//    }
//
//    @Test
//    public void deleteGame() {
//        delete(Addresses.GAMES_ENDPOINT + "/{id}", FIRST_GAME_ID)
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.SC_NO_CONTENT);
//
//        get(Addresses.GAMES_ENDPOINT + "/{id}", FIRST_GAME_ID)
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.SC_NOT_FOUND);
//    }
//
//    @AfterClass
//    public static void tearDown() {
//        RestAssured.reset();
//    }
//
//    private Game getGame() {
//        return new Game();
//    }
}
