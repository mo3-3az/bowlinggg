package com.freeletics.bowlinggg.verticle.game.model;

import com.freeletics.bowlinggg.verticle.game.exception.InvalidPinsNumberException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FrameTest {

    private Frame frame;

    @Before
    public void setUp() {
        frame = new Frame();
    }

    @Test
    public void knockingAllPinsOnFirstTryIsAStrikeFrame() {
        frame.knockPins(Frame.TOTAL_PINS);
        Assert.assertEquals(FrameType.STRIKE, frame.getFrameType());
    }

    @Test
    public void knockingAllPinsOnSecondTryIsASpareFrame() {
        frame.knockPins(0);
        frame.knockPins(10);
        Assert.assertEquals(FrameType.SPARE, frame.getFrameType());
    }

    @Test
    public void knockingAllPinsInTwoTriesIsASpareFrame() {
        frame.knockPins(5);
        frame.knockPins(5);
        Assert.assertEquals(FrameType.SPARE, frame.getFrameType());
    }

    @Test
    public void notKnockingAllPinsAfterTwoTriesIsNormalFrame() {
        frame.knockPins(0);
        frame.knockPins(9);
        Assert.assertEquals(FrameType.NORMAL, frame.getFrameType());
    }

    @Test
    public void gettingAStrikeEndsTheFrame() {
        frame.knockPins(Frame.TOTAL_PINS);

        Assert.assertTrue("The frame has ended!", frame.hasEnded());
    }

    @Test
    public void gettingASpareEndsTheFrame() {
        frame.knockPins(5);
        frame.knockPins(5);

        Assert.assertTrue("The frame has ended!", frame.hasEnded());
    }

    @Test
    public void twoTriesWithPinsLeftEndTheFrame() {
        frame.knockPins(4);
        frame.knockPins(4);

        Assert.assertTrue("The frame has ended!", frame.hasEnded());
    }

    @Test(expected = InvalidPinsNumberException.class)
    public void frameEndsAfterTwoTries() {
        frame.knockPins(0);
        frame.knockPins(9);
        frame.knockPins(1);
    }

    @Test(expected = InvalidPinsNumberException.class)
    public void knockingMoreThanTotalPinsIsNotAllowed() {
        frame.knockPins(Frame.TOTAL_PINS + 1);
    }
}