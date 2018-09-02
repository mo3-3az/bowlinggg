package com.freeletics.bowlinggg.verticle.game;

import io.vertx.core.http.HttpMethod;

/**
 * @author Moath
 */
public enum GameManagerAction {
    GET_GAME,
    CREATE_GAME,
    DELETE_GAME,
    UPDATE_GAME,
    INVALID;

    /**
     * This will translate an http method to an action.
     */
    public static GameManagerAction fromHttpMethod(String httpMethod) {
        if (httpMethod == null || httpMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("httpMethod is blank!");
        }

        switch (HttpMethod.valueOf(httpMethod)) {
            case GET:
                return GET_GAME;

            case POST:
                return CREATE_GAME;

            case PUT:
                return UPDATE_GAME;

            case DELETE:
                return DELETE_GAME;

            default:
                return INVALID;
        }
    }
}
