package com.freeletics.bowlinggg.verticle.game.model;

public enum FrameType {
    STRIKE, SPARE, NORMAL;

    public boolean isStrike() {
        return this == FrameType.STRIKE;
    }

    public boolean isSpare() {
        return this == FrameType.SPARE;
    }

}
