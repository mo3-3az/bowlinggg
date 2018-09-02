package com.freeletics.bowlinggg;

import com.freeletics.bowlinggg.verticle.server.WebServer;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.get;

@RunWith(VertxUnitRunner.class)
public class ApplicationTest extends VertxBasedTest {

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        start(testContext);
    }

    @Test
    public void healthCheck() {
        get(WebServer.HEALTH_CHECK)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stop(testContext);
    }

}