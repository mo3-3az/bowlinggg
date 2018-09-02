package com.freeletics.bowlinggg.verticle.game.model;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game("1");
    }

    /////////// STRIKE ///////////


    @Test
    public void knockingAllPinsOnFirstTryIsAStrike() {
        game.pinsKnocked(Frame.TOTAL_PINS);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.STRIKE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    /////////// SPARE ///////////

    @Test
    public void knockingAllPinsOnSecondTryIsASpare() {
        game.pinsKnocked(0);
        game.pinsKnocked(Frame.TOTAL_PINS);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.SPARE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    @Test
    public void knockingAllPinsInTwoTriesIsASpare() {
        game.pinsKnocked(5);
        game.pinsKnocked(5);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.SPARE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    /////////// ZERO SCORE ///////////

    @Test
    public void notKnockingAllPinsAfterTwoTriesIsNormalFrame() {
        game.pinsKnocked(0);
        game.pinsKnocked(0);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.NORMAL, game.getCurrentFrameType());
        Assert.assertEquals(0, game.getScore());
    }

    /////////// ZERO SCORE ///////////

    @Test
    public void oneStrikeOneNormal() {
        game.pinsKnocked(Frame.TOTAL_PINS);

        game.pinsKnocked(5);
        game.pinsKnocked(2);

        Assert.assertEquals(2, game.getFramesCount());
        Assert.assertEquals(24, game.getScore());
    }

    @Test
    public void twoStrikesOneNormal() {
        game.pinsKnocked(Frame.TOTAL_PINS);
        game.pinsKnocked(Frame.TOTAL_PINS);

        game.pinsKnocked(5);
        game.pinsKnocked(2);

        Assert.assertEquals(3, game.getFramesCount());
        Assert.assertEquals(49, game.getScore());
    }


    ////////////// PERFECT 300 ////////////////////

    @Test
    public void strikeForEveryFrame() {
        for (int i = 0; i < Game.TOTAL_FRAMES; i++) {
            game.pinsKnocked(Frame.TOTAL_PINS);
            Assert.assertTrue("Frame is a strike!", game.getCurrentFrameType().isStrike());
        }

        Assert.assertEquals(Game.TOTAL_FRAMES, game.getFramesCount());

        //Additional two throws
        game.pinsKnocked(Frame.TOTAL_PINS);
        game.pinsKnocked(Frame.TOTAL_PINS);

        //Perfect game
        Assert.assertEquals(300, game.getScore());
        Assert.assertEquals(Game.TOTAL_FRAMES + 2, game.getFramesCount());
    }

    @Test
    public void strikeForEveryFrameExceptSpareOnLast() {
        for (int i = 0; i < Game.TOTAL_FRAMES - 1; i++) {
            game.pinsKnocked(Frame.TOTAL_PINS);
            Assert.assertTrue("Frame is a strike!", game.getCurrentFrameType().isStrike());
        }

        Assert.assertEquals(Game.TOTAL_FRAMES - 1, game.getFramesCount());

        game.pinsKnocked(Frame.TOTAL_PINS - 1);
        game.pinsKnocked(1);

        //Additional one throw
        game.pinsKnocked(Frame.TOTAL_PINS);

        //Perfect game
        Assert.assertEquals(290, game.getScore());
        Assert.assertEquals(Game.TOTAL_FRAMES + 1, game.getFramesCount());
    }

    @Test
    public void jsonConversion() {
        final Game game = new Game("1");
        game.pinsKnocked(10);
        Assert.assertEquals(new JsonObject().put("game"
                , new JsonObject()
                        .put("id", "1")
                        .put("score", 10)
                        .put("frame", "strike")
                        .put("frames", 1))
                .encodePrettily(), game.toJsonString());
    }
}