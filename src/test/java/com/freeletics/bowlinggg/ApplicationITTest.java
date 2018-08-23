package com.freeletics.bowlinggg;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Moath
 */
public class ApplicationITTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("http.port", 8080);
    }

    @Test
    public void startNewGame() {

    }

    @Test
    public void getGame() {

    }

    @Test
    public void updateGame() {

    }

    @AfterClass
    public static void tearDown() {
        RestAssured.reset();
    }

}
