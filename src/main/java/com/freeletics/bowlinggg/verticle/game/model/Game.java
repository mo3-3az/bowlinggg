package com.freeletics.bowlinggg.verticle.game.model;

import io.vertx.core.json.JsonObject;

/**
 * @author Moath
 */
public class Game {

    static final short TOTAL_FRAMES = 10;
    private static final short MAX_FRAMES = 12;

    private String id;

    private int score;
    private int bonus;
    private int framesCount;

    private Frame currentFrame = new Frame();
    private FrameType currentFrameType = currentFrame.getFrameType();

    public Game(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void pinsKnocked(int pins) {
        score += pins;
        addPinsToPreviousFrame(pins);

        currentFrame.knockPins(pins);

        addBonusFromPreviousFrame();

        checkIfFrameHasEnded();
    }

    private void addBonusFromPreviousFrame() {
        if (currentFrame.isStrike() && framesCount < TOTAL_FRAMES) {
            bonus += 2;
        } else if (currentFrame.isSpare() && framesCount < TOTAL_FRAMES) {
            bonus++;
        }
    }

    private void checkIfFrameHasEnded() {
        if (currentFrame.hasEnded()) {
            currentFrameType = currentFrame.getFrameType();
            currentFrame = new Frame();
            framesCount++;
        }

        if (bonus < 0) {
            bonus = 0;
        }
    }

    private void addPinsToPreviousFrame(int pins) {
        if (bonus == 0) {
            return;
        }

        if (framesCount < Game.MAX_FRAMES - 1) {//FRAME BEFORE LAST FRAME
            score += pins;
        }

        if (bonus > 2 && framesCount < Game.TOTAL_FRAMES) { //LAST FRAME
            score += pins;
        }

        bonus--;
    }

    FrameType getCurrentFrameType() {
        return currentFrameType;
    }

    int getFramesCount() {
        return framesCount;
    }

    public String toJsonString() {
        return new JsonObject().put("game"
                , new JsonObject()
                        .put("score", score)
                        .put("frame", currentFrameType.name().toLowerCase())
                        .put("frames", framesCount))
                .encodePrettily();
    }

}
