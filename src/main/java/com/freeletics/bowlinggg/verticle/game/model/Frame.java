package com.freeletics.bowlinggg.verticle.game.model;

/**
 * @author Moath
 */
class Frame {

    static final short TOTAL_PINS = 10;
    private static final short TOTAL_ATTEMPTS = 2;

    private int pinsOnFirstAttempt;
    private int attempt;
    private FrameType frameType = FrameType.NORMAL;

    void knockPins(int knockedPins) {
        if (knockedPins > TOTAL_PINS) {
            throw new IllegalArgumentException("There are only " + TOTAL_PINS + " pins!");
        }

        if (attempt == TOTAL_ATTEMPTS) {
            throw new IllegalStateException("There are only " + TOTAL_ATTEMPTS + " attempts!");
        }

        attempt++;
        if (attempt == 1) {
            pinsOnFirstAttempt = knockedPins;
            if (knockedPins == TOTAL_PINS) {
                frameType = FrameType.STRIKE;
                attempt++;
            }

            return;
        }

        if (pinsOnFirstAttempt + knockedPins == TOTAL_PINS) {
            frameType = FrameType.SPARE;
        }
    }

    FrameType getFrameType() {
        return frameType;
    }

    boolean isStrike() {
        return frameType.isStrike();
    }

    boolean isSpare() {
        return frameType.isSpare();
    }

    boolean hasEnded() {
        return attempt == TOTAL_ATTEMPTS;
    }

}
