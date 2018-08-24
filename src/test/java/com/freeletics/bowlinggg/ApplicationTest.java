package com.freeletics.bowlinggg;

import com.freeletics.bowlinggg.config.Addresses;
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

    private static final String LOCALHOST = "http://localhost/";

    @BeforeClass
    public static void setup(TestContext testContext) throws IOException {
        startVertx(testContext);
    }

    @Test
    public void healthCheck() {
        get(Addresses.HEALTH_CHECK)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        stopVertx(testContext);
    }

}