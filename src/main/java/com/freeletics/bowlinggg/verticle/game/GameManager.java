package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.verticle.Addresses;
import com.freeletics.bowlinggg.verticle.game.store.AsyncInMemGameStore;
import com.freeletics.bowlinggg.verticle.game.store.GameStore;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Moath
 */
public class GameManager extends AbstractVerticle {

    private GameStore gameStore;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        gameStore = new AsyncInMemGameStore(vertx);
    }

    @Override
    public void start(Future<Void> startFuture) {
        final EventBus eventBus = vertx.eventBus();

        eventBus.localConsumer(Addresses.GAMES_ENDPOINT).handler(event -> event.reply(null))
                .completionHandler(newGameConsumer -> chain(startFuture, newGameConsumer));
    }

    private void chain(Future<Void> startFuture, AsyncResult<Void> newGameConsumer) {
        if (newGameConsumer.succeeded()) {
            startFuture.complete();
        } else {
            startFuture.fail(newGameConsumer.cause());
        }
    }

}
