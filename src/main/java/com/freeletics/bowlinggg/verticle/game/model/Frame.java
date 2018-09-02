package com.freeletics.bowlinggg.verticle.game.model;

import com.freeletics.bowlinggg.verticle.game.exception.InvalidPinsNumberException;

/**
 * This class represents a frame in a bowling game.
 *
 * @author Moath
 */
class Frame {

    static final short TOTAL_PINS = 10;
    private static final short TOTAL_ATTEMPTS = 2;

    private int pinsKnockedOnFirstThrow;
    private int attempt;
    private FrameType frameType = FrameType.NORMAL;

    void knockPins(int knockedPins) {
        if (knockedPins > TOTAL_PINS) {
            throw new InvalidPinsNumberException("There are only " + TOTAL_PINS + " pins!");
        }

        if (attempt == TOTAL_ATTEMPTS) {
            throw new InvalidPinsNumberException("There are only " + TOTAL_ATTEMPTS + " attempts!");
        }

        attempt++;
        if (attempt == 1) {
            pinsKnockedOnFirstThrow = knockedPins;
            if (knockedPins == TOTAL_PINS) {
                frameType = FrameType.STRIKE;
                attempt++;
            }

            return;
        }

        if (pinsKnockedOnFirstThrow + knockedPins == TOTAL_PINS) {
            frameType = FrameType.SPARE;
        }
    }

    FrameType getFrameType() {
        return frameType;
    }

    boolean isStrike() {
        return frameType == FrameType.STRIKE;
    }

    boolean isSpare() {
        return frameType == FrameType.SPARE;
    }

    boolean hasEnded() {
        return attempt == TOTAL_ATTEMPTS;
    }

}
