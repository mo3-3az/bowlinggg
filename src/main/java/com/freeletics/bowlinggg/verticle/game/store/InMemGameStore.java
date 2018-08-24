package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Moath
 */
public class InMemGameStore implements GameStore {

    private static final Logger LOG = Logger.getLogger(InMemGameStore.class);

    private static final String GAMES_MAP = "games";

    private Map<String, Game> games;

    private AtomicLong idSequence;

    public InMemGameStore() {
        games = new HashMap<>();
        idSequence = new AtomicLong(0);
    }

    @Override
    public Game newGame() {
        final String id = String.valueOf(idSequence.incrementAndGet());
        final Game game = new Game(id);
        games.put(id, game);
        return game;
    }

    @Override
    public Game deleteGame(String id) {
        return games.remove(id);
    }

    @Override
    public Game updateGame(Game game) {
        return games.put(game.getId(), game);
    }

    @Override
    public Game getGame(String id) {
        return games.get(id);
    }
}
