package com.freeletics.bowlinggg.verticle.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moath
 */
public class Game {

    private String id;
    private int score;
    private List<Frame> frames = new ArrayList<>();
    private int throwCount;
    private FrameType lastFrameType;

    public Game(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void update(int pins) {
        final Frame frame;
        if (newFrame()) {
            frame = new Frame();
            frames.add(frame);
        } else {
            frame = getLastFrame();
        }

        frame.knockPins(pins);

        if (newFrame()) {
            score += frame.getPinsOnFirstAttempt();
        } else {
            score += frame.getPinsOnSecondAttempt();
        }

        //Update
        frames.set(frames.size() - 1, frame);

        lastFrameType = frame.getFrameType();
        if (frame.getFrameType().isStirke()) {
            endFrame();
            return;
        }

        throwCount++;
        updateScoreFromPreviousFrame();
    }

    private void endFrame() {
        throwCount += 2;
    }

    private boolean newFrame() {
        return throwCount % 2 == 0;
    }

    private void updateScoreFromPreviousFrame() {
        if (frames.size() == 1) {
            return;
        }

        if (lastFrameType.isStirke() || lastFrameType.isSpare()) {
            score += getPreviousFrame().getPinsOnFirstAttempt();
        }

        if (lastFrameType.isStirke()) {
            score += getPreviousFrame().getPinsOnSecondAttempt();
        }
    }

    private Frame getLastFrame() {
        return frames.get(frames.size() - 1);
    }

    private Frame getPreviousFrame() {
        return frames.get(frames.size() - 2);
    }

}
