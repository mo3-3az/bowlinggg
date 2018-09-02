package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;

public interface GameStore {

    /**
     * This should create a new game and return it.
     */
    Game newGame();

    /**
     * This should delete the game which it's ID was passed.
     */
    void deleteGame(String id);

    /**
     * This should update the game which it's reference was passed.
     */
    void updateGame(Game game);

    /**
     * This should get the game which it's ID was passed and return it.
     */
    Game getGame(String id);
}
