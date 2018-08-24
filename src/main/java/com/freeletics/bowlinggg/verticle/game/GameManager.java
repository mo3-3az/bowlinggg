package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.config.Addresses;
import com.freeletics.bowlinggg.verticle.game.store.GameStore;
import com.freeletics.bowlinggg.verticle.game.store.InMemGameStore;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.log4j.Logger;

/**
 * @author Moath
 */
public class GameManager extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(GameManager.class);

    private GameStore gameStore;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        gameStore = new InMemGameStore();
    }

    @Override
    public void start(Future<Void> startFuture) {
        final EventBus eventBus = vertx.eventBus();

        eventBus.localConsumer(Addresses.GAMES_ENDPOINT).handler(this::replyToMsg)
                .completionHandler(newGameConsumer -> chain(startFuture, newGameConsumer));
    }

    private void replyToMsg(Message<Object> event) {
        event.reply(null);
    }

    private void chain(Future<Void> startFuture, AsyncResult<Void> newGameConsumer) {
        if (newGameConsumer.succeeded()) {
            LOG.info(getClass().getSimpleName() + " was deployed successfully.");
            startFuture.complete();
        } else {
            startFuture.fail(newGameConsumer.cause());
        }
    }

}
