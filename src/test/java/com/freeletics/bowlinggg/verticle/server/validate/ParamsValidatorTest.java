package com.freeletics.bowlinggg.verticle.server.validate;

import org.junit.Assert;
import org.junit.Test;

public class ParamsValidatorTest {

    @Test
    public void invalidId() {
        Assert.assertTrue("XX is an invalid id!", ParamsValidator.invalidId("XX"));
    }

    @Test
    public void invalidPins() {
        Assert.assertTrue("XX is an invalid id!", ParamsValidator.invalidPins("XX"));
    }

    @Test
    public void validId() {
        Assert.assertTrue("123 is a valid id!", !ParamsValidator.invalidId("123"));
    }

    @Test
    public void validPins() {
        Assert.assertTrue("10 is a valid pins value!", !ParamsValidator.invalidPins("10"));
    }

    @Test
    public void nullId() {
        Assert.assertTrue("null is an invalid pins value!", ParamsValidator.invalidId(null));
    }

    @Test
    public void nullPins() {
        Assert.assertTrue("null is an invalid pins value!", ParamsValidator.invalidPins(null));

    }
}