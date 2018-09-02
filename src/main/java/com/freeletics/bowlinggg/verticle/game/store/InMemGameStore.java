package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation stores games info in a in-memory concurrent map.
 *
 * @author Moath
 */
public class InMemGameStore implements GameStore {

    private Map<String, Game> games;

    private AtomicLong idSequence;

    public InMemGameStore() {
        games = new ConcurrentHashMap<>();
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
    public void deleteGame(String id) {
        games.remove(id);
    }

    @Override
    public void updateGame(Game game) {
        games.put(game.getId(), game);
    }

    @Override
    public Game getGame(String id) {
        return games.get(id);
    }
}
