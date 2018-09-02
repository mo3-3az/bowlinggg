package com.freeletics.bowlinggg.verticle.game.model;

import com.freeletics.bowlinggg.verticle.game.exception.GameHasFinishedException;
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
        game.knockPins(Frame.TOTAL_PINS);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.STRIKE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    /////////// SPARE ///////////

    @Test
    public void knockingAllPinsOnSecondTryIsASpare() {
        game.knockPins(0);
        game.knockPins(Frame.TOTAL_PINS);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.SPARE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    @Test
    public void knockingAllPinsInTwoTriesIsASpare() {
        game.knockPins(5);
        game.knockPins(5);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.SPARE, game.getCurrentFrameType());
        Assert.assertEquals(Frame.TOTAL_PINS, game.getScore());
    }

    /////////// ZERO SCORE ///////////

    @Test
    public void notKnockingAllPinsAfterTwoTriesIsNormalFrame() {
        game.knockPins(0);
        game.knockPins(0);
        Assert.assertEquals(1, game.getFramesCount());

        Assert.assertEquals(FrameType.NORMAL, game.getCurrentFrameType());
        Assert.assertEquals(0, game.getScore());
    }

    /////////// ZERO SCORE ///////////

    @Test
    public void oneStrikeOneNormal() {
        game.knockPins(Frame.TOTAL_PINS);

        game.knockPins(5);
        game.knockPins(2);

        Assert.assertEquals(2, game.getFramesCount());
        Assert.assertEquals(24, game.getScore());
    }

    @Test
    public void twoStrikesOneNormal() {
        game.knockPins(Frame.TOTAL_PINS);
        game.knockPins(Frame.TOTAL_PINS);

        game.knockPins(5);
        game.knockPins(2);

        Assert.assertEquals(3, game.getFramesCount());
        Assert.assertEquals(49, game.getScore());
    }


    ////////////// PERFECT 300 ////////////////////

    @Test
    public void strikeForEveryFrame() {
        for (int i = 0; i < Game.TOTAL_FRAMES; i++) {
            game.knockPins(Frame.TOTAL_PINS);
            Assert.assertEquals(FrameType.STRIKE, game.getCurrentFrameType());
        }

        Assert.assertEquals(Game.TOTAL_FRAMES, game.getFramesCount());

        //Additional two throws
        game.knockPins(Frame.TOTAL_PINS);
        game.knockPins(Frame.TOTAL_PINS);

        //Perfect game
        Assert.assertEquals(300, game.getScore());
        Assert.assertEquals(Game.TOTAL_FRAMES + 2, game.getFramesCount());
    }

    @Test
    public void strikeForEveryFrameExceptSpareOnLast() {
        for (int i = 0; i < Game.TOTAL_FRAMES - 1; i++) {
            game.knockPins(Frame.TOTAL_PINS);
            Assert.assertEquals(FrameType.STRIKE, game.getCurrentFrameType());
        }

        Assert.assertEquals(Game.TOTAL_FRAMES - 1, game.getFramesCount());

        game.knockPins(Frame.TOTAL_PINS - 1);
        game.knockPins(1);

        //Additional one throw
        game.knockPins(Frame.TOTAL_PINS);

        //Perfect game
        Assert.assertEquals(290, game.getScore());
        Assert.assertEquals(Game.TOTAL_FRAMES + 1, game.getFramesCount());
    }

    @Test(expected = GameHasFinishedException.class)
    public void gameMustFinishAtTenFramesIfLastFrameIsNotSpareOrNotStrike() {
        for (int i = 0; i < 21; i++) {// 21 throws with not a single spare or strike frame
            game.knockPins(1);
        }
    }

    @Test(expected = GameHasFinishedException.class)
    public void gameMustFinishAtElevenFramesIfLastFrameIsNotStrike() {
        for (int i = 0; i < Game.TOTAL_FRAMES - 1; i++) {// 9 strikes!
            game.knockPins(Frame.TOTAL_PINS);
        }

        //Last frame is a spare
        game.knockPins(5);
        game.knockPins(5);

        //Only one more throw!
        game.knockPins(5);

        //This is not allowed!
        game.knockPins(5);
    }

    @Test(expected = GameHasFinishedException.class)
    public void gameMaximumFramesAreTwelve() {
        for (int i = 0; i < Game.TOTAL_FRAMES + 2; i++) {// 12 strikes!
            game.knockPins(Frame.TOTAL_PINS);
        }

        //This is not allowed!
        game.knockPins(5);
    }

    @Test
    public void jsonConversion() {
        final Game game = new Game("1");
        game.knockPins(10);
        Assert.assertEquals(new JsonObject().put("game"
                , new JsonObject()
                        .put("id", "1")
                        .put("score", 10)
                        .put("frame", "strike")
                        .put("frames", 1))
                .encodePrettily(), game.toJsonString());
    }
}