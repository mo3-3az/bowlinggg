package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;
import org.apache.log4j.Logger;

/**
 * @author Moath
 */
public class AsyncInMemGameStore implements GameStore {

    private static final Logger LOG = Logger.getLogger(AsyncInMemGameStore.class);

    private static final String GAMES_MAP = "games";

    private AsyncMap<String, Game> games;

    public AsyncInMemGameStore(Vertx vertx) {
        vertx.sharedData().<String, Game>getAsyncMap(GAMES_MAP, event -> {
            if (event.succeeded()) {
                games = event.result();
            } else {
                LOG.error("Something went wrong initializing the games store!", event.cause());
            }
        });
    }

    @Override
    public Game newGame() {
        return null;
    }

    @Override
    public Game deleteGame() {
        return null;
    }

    @Override
    public Game updateGame(Game game) {
        return null;
    }

    @Override
    public Game getGame(String id) {
        return null;
    }
}
