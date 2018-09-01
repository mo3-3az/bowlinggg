package com.freeletics.bowlinggg.verticle.game;

import org.junit.Assert;
import org.junit.Test;

public class GameManagerActionTest {

    @Test
    public void fromPostHttpMethod() {
        Assert.assertEquals(GameManagerAction.CREATE_GAME, GameManagerAction.fromHttpMethod("POST"));
    }

    @Test
    public void fromGetHttpMethod() {
        Assert.assertEquals(GameManagerAction.GET_GAME, GameManagerAction.fromHttpMethod("GET"));
    }

    @Test
    public void fromPutHttpMethod() {
        Assert.assertEquals(GameManagerAction.UPDATE_GAME, GameManagerAction.fromHttpMethod("PUT"));
    }

    @Test
    public void fromDeleteHttpMethod() {
        Assert.assertEquals(GameManagerAction.DELETE_GAME, GameManagerAction.fromHttpMethod("DELETE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNullHttpMethod() {
        GameManagerAction.fromHttpMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidHttpMethod() {
        GameManagerAction.fromHttpMethod("INVALID");
    }
}