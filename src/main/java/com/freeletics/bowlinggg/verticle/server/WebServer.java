package com.freeletics.bowlinggg.verticle.server;


import com.freeletics.bowlinggg.config.Addresses;
import com.freeletics.bowlinggg.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
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
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    @Override
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, Addresses.HEALTH_CHECK).handler(context -> context.response().setStatusCode(HttpStatus.SC_OK).end());

        router.route(HttpMethod.GET, Addresses.GAMES_ENDPOINT_FULL).handler(requestHandler());
        router.route(HttpMethod.POST, Addresses.GAMES_ENDPOINT).handler(requestHandler());
        router.route(HttpMethod.DELETE, Addresses.GAMES_ENDPOINT_FULL).handler(requestHandler());
        router.route(HttpMethod.PUT, Addresses.GAMES_ENDPOINT_FULL + Addresses.PATH_PART_PARAM_PINS).handler(requestHandler());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(vertx.getOrCreateContext().config().getInteger(Config.HTTP_PORT), result -> {
                    if (result.succeeded()) {
                        LOG.info(getClass().getSimpleName() + " was deployed successfully.");
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    private Handler<RoutingContext> requestHandler() {
        return routingContext -> {
            final HttpServerRequest request = routingContext.request();
            final DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.addHeader(Addresses.EVENT_BUS_MSG_HEADER_ACTION, request.rawMethod());

            if (HttpMethod.PUT.equals(request.method())) {
                deliveryOptions.addHeader(Addresses.PARAM_PINS, request.getParam(Addresses.PARAM_PINS));
            }

            if (!HttpMethod.POST.equals(request.method())) {
                deliveryOptions.addHeader(Addresses.PARAM_ID, request.getParam(Addresses.PARAM_ID));
            }

            vertx.eventBus().send(Addresses.GAMES_ENDPOINT, null, deliveryOptions, event -> respond(routingContext, request, event));
        };
    }

    private void respond(RoutingContext routingContext, HttpServerRequest request, AsyncResult<Message<Object>> event) {
        if (event.failed()) {
            routingContext
                    .response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                    .setStatusCode(HttpStatus.SC_BAD_REQUEST)
                    .end(new JsonObject()
                            .put("error", true)
                            .put("msg", event.cause().getMessage())
                            .encodePrettily());
            return;
        }

        int statusCode = HttpStatus.SC_OK;
        String responseBody = "";
        switch (request.method()) {
            case GET:
                responseBody = event.result().body().toString();
                break;

            case POST:
                statusCode = HttpStatus.SC_CREATED;
                break;

            case PUT:
            case DELETE:
                statusCode = HttpStatus.SC_NO_CONTENT;
                break;
        }

        routingContext
                .response()
                .putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                .setStatusCode(statusCode)
                .end(responseBody);
    }

}
