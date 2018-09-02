package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.VertxBasedTest;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class GameManagerTest extends VertxBasedTest {

    private static final String DELETE_GAME_TEST_CASE = "deleteGame";
    private static final String GET_GAME_TEST_CASE = "getGame";
    private static final String UPDATE_GAME_TEST_CASE = "updateGame";
    private static final String START_NEW_GAME_TEST_CASE = "startNewGame";

    private static final String NO_MESSAGE = null;

    private static final String GAME_ID = "1";
    private static final String ONE_PIN = "1";

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        start(testContext);
    }

    @Test
    public void testGameManager(TestContext testContext) {
        Async async1 = testContext.async();
        Async async2 = testContext.async();
        Async async3 = testContext.async();
        Async async4 = testContext.async();

        TestSuite.create(getClass().getSimpleName())
                .test(START_NEW_GAME_TEST_CASE, event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(GameManager.EVENT_BUS_MSG_HEADER_ACTION, HttpMethod.POST.name());
                    eventBus().send(GameManager.EVENT_BUS_ADDRESS_GAMES_MANAGER, NO_MESSAGE, deliveryOptions, newGameEvent -> {
                        if (newGameEvent.failed()) {
                            testContext.fail(newGameEvent.cause().getMessage());
                        } else {
                            async1.complete();
                        }
                    });
                })
                .test(UPDATE_GAME_TEST_CASE, event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(GameManager.EVENT_BUS_MSG_HEADER_ACTION, HttpMethod.PUT.name());
                    deliveryOptions.addHeader(GameManager.HEADER_ID, GAME_ID);
                    deliveryOptions.addHeader(GameManager.HEADER_PINS, ONE_PIN);
                    eventBus().send(GameManager.EVENT_BUS_ADDRESS_GAMES_MANAGER, NO_MESSAGE, deliveryOptions, updateGameEvent -> {
                        async1.awaitSuccess();
                        if (updateGameEvent.failed()) {
                            testContext.fail(updateGameEvent.cause().getMessage());
                        } else {
                            async2.complete();
                        }
                    });
                })
                .test(GET_GAME_TEST_CASE, event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(GameManager.EVENT_BUS_MSG_HEADER_ACTION, HttpMethod.GET.name());
                    deliveryOptions.addHeader(GameManager.HEADER_ID, GAME_ID);
                    eventBus().send(GameManager.EVENT_BUS_ADDRESS_GAMES_MANAGER, NO_MESSAGE, deliveryOptions, getGameEvent -> {
                        async2.awaitSuccess();
                        if (getGameEvent.failed()) {
                            testContext.fail(getGameEvent.cause().getMessage());
                        } else {
                            if (getGameEvent.result() == null) {
                                testContext.fail(getGameEvent.cause().getMessage());
                            } else {
                                async3.complete();
                            }
                        }
                    });
                })
                .test(DELETE_GAME_TEST_CASE, event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(GameManager.EVENT_BUS_MSG_HEADER_ACTION, HttpMethod.DELETE.name());
                    deliveryOptions.addHeader(GameManager.HEADER_ID, GAME_ID);
                    eventBus().send(GameManager.EVENT_BUS_ADDRESS_GAMES_MANAGER, NO_MESSAGE, deliveryOptions, deleteGameEvent -> {
                        async3.awaitSuccess();
                        if (deleteGameEvent.failed()) {
                            testContext.fail(deleteGameEvent.cause().getMessage());
                        } else {
                            async4.complete();
                        }
                    });
                })
                .run(vertx, new TestOptions());

        async4.awaitSuccess();
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stop(testContext);
    }

}