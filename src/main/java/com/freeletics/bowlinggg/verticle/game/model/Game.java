package com.freeletics.bowlinggg.verticle.game.model;

import com.freeletics.bowlinggg.verticle.game.exception.GameHasFinishedException;
import io.vertx.core.json.JsonObject;

/**
 * @author Moath
 */
public class Game {

    public static final String JSON_KEY_GAME = "game";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_SCORE = "score";
    private static final String JSON_KEY_FRAME = "frame";
    private static final String JSON_KEY_FRAMES = "frames";

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

    public void knockPins(int pins) {
        checkIfGameHasFinished();

        calculateScore(pins);

        currentFrame.knockPins(pins);

        addBonusFromCurrentFrame();

        checkIfCurrentFrameHasEnded();
    }

    private void checkIfGameHasFinished() {
        if (framesCount == MAX_FRAMES) {
            throw new GameHasFinishedException("Game has finished! Maximum frames to play " + MAX_FRAMES);
        }

        if (framesCount == TOTAL_FRAMES && currentFrameType == FrameType.NORMAL) {
            throw new GameHasFinishedException("Game has finished at " + TOTAL_FRAMES + " frames!");
        }

        if (framesCount == TOTAL_FRAMES + 1 && currentFrameType != FrameType.STRIKE) {
            throw new GameHasFinishedException("Game has finished at " + TOTAL_FRAMES + " frames!");
        }

    }

    private void addBonusFromCurrentFrame() {
        if (framesCount > TOTAL_FRAMES) {
            return;
        }

        if (currentFrame.isStrike()) {
            bonus += 2;
        } else if (currentFrame.isSpare()) {
            bonus++;
        }
    }

    private void checkIfCurrentFrameHasEnded() {
        if (currentFrame.hasEnded() || (framesCount == TOTAL_FRAMES && currentFrameType == FrameType.SPARE)) {
            currentFrameType = currentFrame.getFrameType();
            currentFrame = new Frame();
            framesCount++;
        }

        if (bonus < 0) {
            bonus = 0;
        }
    }

    private void calculateScore(int pins) {
        score += pins;

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
        return new JsonObject().put(JSON_KEY_GAME
                , new JsonObject()
                        .put(JSON_KEY_ID, id)
                        .put(JSON_KEY_SCORE, score)
                        .put(JSON_KEY_FRAME, currentFrameType.name().toLowerCase())
                        .put(JSON_KEY_FRAMES, framesCount))
                .encodePrettily();
    }

}
