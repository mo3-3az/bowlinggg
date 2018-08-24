package com.freeletics.bowlinggg.verticle;

import com.freeletics.bowlinggg.VertxBasedTest;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
public class DeployerVerticleTest extends VertxBasedTest {

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        startVertx(testContext);
    }

    @Test
    public void webServerVerticleWasDeployed() {
    }

    @Test
    public void gameManagerVerticleWasDeployed() {
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stopVertx(testContext);
    }
}