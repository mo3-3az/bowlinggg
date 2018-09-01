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

    public static GameManagerAction fromHttpMethod(String httpMethod) {
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
