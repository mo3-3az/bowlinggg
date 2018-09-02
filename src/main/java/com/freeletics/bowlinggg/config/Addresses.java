package com.freeletics.bowlinggg.config;

/**
 * @author Moath
 */
public interface Addresses {

    String EVENT_BUS_MSG_HEADER_ACTION = "action";
    String EVENT_BUS_ADDRESS_GAMES_MANAGER = "games-manager";

    String HEALTH_CHECK = "/healthcheck";
    String API_VERSION = "/v1";

    String PARAM_ID = "id";
    String PATH_PART_PARAM_ID = "/:" + PARAM_ID;
    String PARAM_PINS = "pins";
    String PATH_PART_PARAM_PINS = "/:" + PARAM_PINS;

    String GAMES_ENDPOINT = API_VERSION + "/games";
    String GAMES_ENDPOINT_FULL = GAMES_ENDPOINT + PATH_PART_PARAM_ID;

}
