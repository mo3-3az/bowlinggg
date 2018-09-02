package com.freeletics.bowlinggg.verticle.server.validate;

import java.util.regex.Pattern;

/**
 * @author Moath
 */
public class ParamsValidator {

    private static final String INTEGER_NUMBER_REGEX = "\\d+";

    private ParamsValidator() {
    }

    public static boolean invalidId(String paramID) {
        if (paramID == null) {
            return true;
        }

        return !Pattern.matches(INTEGER_NUMBER_REGEX, paramID);
    }

    public static boolean invalidPins(String paramPins) {
        if (paramPins == null) {
            return true;
        }

        return !Pattern.matches(INTEGER_NUMBER_REGEX, paramPins);
    }
}
