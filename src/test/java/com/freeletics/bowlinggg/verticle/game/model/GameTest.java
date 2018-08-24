package com.freeletics.bowlinggg.verticle.game.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game("1");
    }

    @Test
    public void knockingAllPinsOnFirstTryIsAStrike() {
        game.update(Frame.TOTAL_PINS);
        Assert.assertEquals(1, game.getFrames().size());

        final Optional<Frame> first = game.getFrames().stream().findFirst();
        if (first.isPresent()) {
            Assert.assertEquals(FrameType.STRIKE, first.get().getFrameType());
            Assert.assertEquals(10, game.getScore());
            return;
        }

        Assert.fail("A new strike frame should be there!");
    }

    @Test
    public void knockingAllPinsOnSecondTryIsASpare() {
        game.update(0);
        game.update(10);
        Assert.assertEquals(1, game.getFrames().size());

        final Optional<Frame> first = game.getFrames().stream().findFirst();
        if (first.isPresent()) {
            Assert.assertEquals(FrameType.SPARE, first.get().getFrameType());
            Assert.assertEquals(10, game.getScore());
            return;
        }

        Assert.fail("A new spare frame should be there!");
    }

    @Test
    public void knockingAllPinsInTwoTriesIsASpare() {
        game.update(5);
        game.update(5);
        Assert.assertEquals(1, game.getFrames().size());

        final Optional<Frame> first = game.getFrames().stream().findFirst();
        if (first.isPresent()) {
            Assert.assertEquals(FrameType.SPARE, first.get().getFrameType());
            Assert.assertEquals(10, game.getScore());
            return;
        }

        Assert.fail("A new spare frame should be there!");
    }

    @Test
    public void notKnockingAllPinsAfterTwoTriesIsNormalFrame() {
        game.update(0);
        game.update(0);
        Assert.assertEquals(1, game.getFrames().size());

        final Optional<Frame> first = game.getFrames().stream().findFirst();
        if (first.isPresent()) {
            Assert.assertEquals(FrameType.NORMAL, first.get().getFrameType());
            Assert.assertEquals(0, game.getScore());
            return;
        }

        Assert.fail("A new normal frame should be there!");
    }

}