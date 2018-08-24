package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class InMemGameStoreTest {

    private static final String FIRST_GAME_ID = "1";
    private InMemGameStore inMemGameStore;

    @Before
    public void setUp() {
        inMemGameStore = new InMemGameStore();
    }

    @Test
    public void newGame() {
        final Game game = inMemGameStore.newGame();
        Assert.assertEquals(FIRST_GAME_ID, game.getId());
        Assert.assertEquals(Collections.emptyList(), game.getFrames());
        Assert.assertEquals(0, game.getScore());
    }

    @Test
    public void deleteGame() {
        final Game game = inMemGameStore.newGame();
        inMemGameStore.deleteGame(FIRST_GAME_ID);
        Assert.assertEquals(FIRST_GAME_ID, game.getId());
        Assert.assertEquals(Collections.emptyList(), game.getFrames());
        Assert.assertEquals(0, game.getScore());
        Assert.assertNull(inMemGameStore.getGame(FIRST_GAME_ID));
    }

    @Test
    public void updateGame() {
        //TODO: Implement
    }

    @Test
    public void getGame() {
        final Game game = inMemGameStore.newGame();
        final Game gameById = inMemGameStore.getGame(FIRST_GAME_ID);
        Assert.assertEquals(game.getId(), gameById.getId());
        Assert.assertEquals(game.getFrames(), gameById.getFrames());
        Assert.assertEquals(game.getScore(), gameById.getScore());
    }

}