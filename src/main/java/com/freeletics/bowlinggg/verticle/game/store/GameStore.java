package com.freeletics.bowlinggg.verticle.game.store;

import com.freeletics.bowlinggg.verticle.game.model.Game;

public interface GameStore {

    Game newGame();

    Game deleteGame(String id);

    Game updateGame(Game game);

    Game getGame(String id);
}
