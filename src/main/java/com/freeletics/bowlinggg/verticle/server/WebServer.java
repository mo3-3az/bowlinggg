package com.freeletics.bowlinggg.verticle.server;


import com.freeletics.bowlinggg.config.Config;
import com.freeletics.bowlinggg.verticle.game.GameManager;
import com.freeletics.bowlinggg.verticle.server.validate.ParamsValidator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * This is the main verticle, here an implementation of a web server will be done.
 *
 * @author Moath
 */
public class WebServer extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(WebServer.class);

    public static final String HEALTH_CHECK = "/healthcheck";

    private static final String API_VERSION = "/v1";
    static final String GAMES_ENDPOINT = API_VERSION + "/games";
    private static final String PARAM_ID = "id";
    private static final String PATH_PART_PARAM_ID = "/:" + PARAM_ID;
    private static final String PARAM_PINS = "pins";
    private static final String PATH_PART_PARAM_PINS = "/:" + PARAM_PINS;
    private static final String GAMES_ENDPOINT_FULL = GAMES_ENDPOINT + PATH_PART_PARAM_ID;

    private static final String NO_MESSAGE = null;
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final String MSG_INVALID_PINS_VALUE = "Invalid pins value!";
    private static final String MSG_INVALID_ID_VALUE = "Invalid id value!";

    @Override
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, HEALTH_CHECK).handler(context -> context.response().setStatusCode(HttpStatus.SC_OK).end());

        router.route(HttpMethod.POST, GAMES_ENDPOINT).handler(requestHandler());
        router.route(HttpMethod.GET, GAMES_ENDPOINT_FULL).handler(requestHandler());
        router.route(HttpMethod.DELETE, GAMES_ENDPOINT_FULL).handler(requestHandler());
        router.route(HttpMethod.PUT, GAMES_ENDPOINT_FULL + PATH_PART_PARAM_PINS).handler(requestHandler());

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(vertx.getOrCreateContext().config().getInteger(Config.HTTP_PORT), result -> {
                    if (result.succeeded()) {
                        LOG.info(getClass().getSimpleName() + " was deployed successfully.");
                        future.complete();
                    } else {
                        LOG.error(getClass().getSimpleName() + " wasn't deployed successfully.", result .cause());
                        future.fail(result.cause());
                    }
                });
    }

    private Handler<RoutingContext> requestHandler() {
        return routingContext -> {
            final HttpServerRequest request = routingContext.request();
            final HttpMethod method = request.method();
            final DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.addHeader(GameManager.EVENT_BUS_MSG_HEADER_ACTION, method.name());

            if (HttpMethod.PUT.equals(method)) {
                final String pins = request.getParam(PARAM_PINS);
                if (ParamsValidator.invalidPins(pins)) {
                    replyWithError(routingContext.response(), MSG_INVALID_PINS_VALUE);
                    return;
                }

                deliveryOptions.addHeader(PARAM_PINS, pins);
            }

            if (!HttpMethod.POST.equals(method)) {
                final String id = request.getParam(PARAM_ID);
                if (ParamsValidator.invalidId(id)) {
                    replyWithError(routingContext.response(), MSG_INVALID_ID_VALUE);
                    return;
                }

                deliveryOptions.addHeader(PARAM_ID, id);
            }

            vertx.eventBus().send(GameManager.EVENT_BUS_ADDRESS_GAMES_MANAGER, NO_MESSAGE, deliveryOptions, event -> reply(routingContext, event));
        };
    }

    private void reply(RoutingContext routingContext, AsyncResult<Message<Object>> event) {
        routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        if (event.failed()) {
            final ReplyException cause = (ReplyException) event.cause();
            replyWithError(routingContext.response(), cause.failureCode(), cause.getMessage());
            return;
        }

        int statusCode = HttpStatus.SC_OK;
        String responseBody = "";
        switch (routingContext.request().method()) {
            case GET:
                responseBody = event.result().body().toString();
                break;

            case POST:
                statusCode = HttpStatus.SC_CREATED;
                responseBody = event.result().body().toString();
                break;

            case PUT:
            case DELETE:
                statusCode = HttpStatus.SC_NO_CONTENT;
                break;
        }

        routingContext
                .response()
                .setStatusCode(statusCode)
                .end(responseBody);
    }

    private void replyWithError(HttpServerResponse response, String msg) {
        replyWithError(response, HttpStatus.SC_BAD_REQUEST, msg);
    }

    private void replyWithError(HttpServerResponse response, int code, String msg) {
        response.setStatusCode(code).end(getError(msg));
    }

    private String getError(String message) {
        return new JsonObject()
                .put("error", true)
                .put("msg", message)
                .encodePrettily();
    }

}
