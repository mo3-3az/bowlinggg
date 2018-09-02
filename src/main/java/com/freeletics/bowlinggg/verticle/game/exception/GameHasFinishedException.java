package com.freeletics.bowlinggg.verticle.game.exception;

/**
 * @author Moath
 */
public class GameHasFinishedException extends IllegalStateException {
    public GameHasFinishedException(String s) {
        super(s);
    }
}
