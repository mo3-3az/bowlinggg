package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.VertxBasedTest;
import com.freeletics.bowlinggg.config.Addresses;
import io.vertx.core.eventbus.DeliveryOptions;
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
                .test("startNewGame", event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(Addresses.EVENT_BUS_MSG_HEADER_ACTION, "POST");
                    eventBus().send(Addresses.GAMES_ENDPOINT, null, deliveryOptions, newGameEvent -> {
                        if (newGameEvent.failed()) {
                            testContext.fail(newGameEvent.cause().getMessage());
                        } else {
                            async1.complete();
                        }
                    });
                })
                .test("updateGame", event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(Addresses.EVENT_BUS_MSG_HEADER_ACTION, "PUT");
                    deliveryOptions.addHeader(Addresses.PARAM_ID, "1");
                    deliveryOptions.addHeader(Addresses.PARAM_PINS, "1");
                    eventBus().send(Addresses.GAMES_ENDPOINT, null, deliveryOptions, updateGameEvent -> {
                        async1.awaitSuccess();
                        if (updateGameEvent.failed()) {
                            testContext.fail(updateGameEvent.cause().getMessage());
                        } else {
                            async2.complete();
                        }
                    });
                })
                .test("getGame", event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(Addresses.EVENT_BUS_MSG_HEADER_ACTION, "GET");
                    deliveryOptions.addHeader(Addresses.PARAM_ID, "1");
                    eventBus().send(Addresses.GAMES_ENDPOINT, null, deliveryOptions, getGameEvent -> {
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
                .test("deleteGame", event -> {
                    final DeliveryOptions deliveryOptions = new DeliveryOptions();
                    deliveryOptions.addHeader(Addresses.EVENT_BUS_MSG_HEADER_ACTION, "DELETE");
                    deliveryOptions.addHeader(Addresses.PARAM_ID, "1");
                    eventBus().send(Addresses.GAMES_ENDPOINT, null, deliveryOptions, deleteGameEvent -> {
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