package com.freeletics.bowlinggg.verticle.game.model;

public enum FrameType {
    STRIKE, SPARE, NORMAL;

    public boolean isStirke() {
        return this == FrameType.STRIKE;
    }

    public boolean isSpare() {
        return this == FrameType.SPARE;
    }

    public boolean isNormal() {
        return this == FrameType.NORMAL;
    }
}
