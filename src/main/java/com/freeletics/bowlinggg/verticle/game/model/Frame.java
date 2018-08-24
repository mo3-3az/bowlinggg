package com.freeletics.bowlinggg.verticle.game.model;

/**
 * @author Moath
 */
public class Frame {

    public static final short TOTAL_PINS = 10;
    public static final short TOTAL_TRIES = 2;

    private int pinsOnSecondAttempt;
    private int pinsOnFirstAttempt;
    private int attempt;
    private FrameType frameType = FrameType.NORMAL;

    public void knockPins(int knockedPins) {
        if (knockedPins > TOTAL_PINS) {
            throw new IllegalArgumentException("There are only " + TOTAL_PINS + " pins!");
        }

        if (attempt == TOTAL_TRIES) {
            throw new IllegalStateException("There are only " + TOTAL_TRIES + " tries!");
        }

        attempt++;
        if (attempt == 1) {
            pinsOnFirstAttempt = knockedPins;
            if (knockedPins == TOTAL_PINS) {
                frameType = FrameType.STRIKE;
            }

            return;
        }

        pinsOnSecondAttempt = knockedPins;

        if (pinsOnFirstAttempt + pinsOnSecondAttempt == TOTAL_PINS) {
            frameType = FrameType.SPARE;
        }
    }

    public FrameType getFrameType() {
        return frameType;
    }

    public int getPinsOnSecondAttempt() {
        return pinsOnSecondAttempt;
    }

    public int getPinsOnFirstAttempt() {
        return pinsOnFirstAttempt;
    }

}
