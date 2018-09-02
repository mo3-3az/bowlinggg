package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.config.Addresses;
import com.freeletics.bowlinggg.verticle.game.model.Game;
import com.freeletics.bowlinggg.verticle.game.store.GameStore;
import com.freeletics.bowlinggg.verticle.game.store.InMemGameStore;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import org.apache.log4j.Logger;

/**
 * This will receive actions related to the game logic.
 * Basically, it will register a consumer that will listen on
 * a specific address and handle those messages.
 *
 * @author Moath
 */
public class GameManager extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(GameManager.class);

    private static final int FAILURE_CODE_GAME_NOT_FOUND = 404;
    private static final int FAILURE_CODE_INVALID_INPUT = 400;

    private static final String EMPTY_MESSAGE = "";
    private static final String MSG_GAME_NOT_FOUND = "Game not found!";
    private static final String MSG_INVALID_ACTION = "Invalid action!";

    private GameStore gameStore;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        gameStore = new InMemGameStore();
    }

    @Override
    public void start(Future<Void> startFuture) {
        vertx
                .eventBus()
                .localConsumer(Addresses.EVENT_BUS_ADDRESS_GAMES_MANAGER)
                .handler(this::replyToMsg)
                .completionHandler(newGameConsumer -> chain(startFuture, newGameConsumer));
    }

    private void replyToMsg(Message<Object> event) {
        final String id = event.headers().get(Addresses.PARAM_ID);
        if (id != null && gameStore.getGame(id) == null) {
            event.fail(FAILURE_CODE_GAME_NOT_FOUND, MSG_GAME_NOT_FOUND);
            return;
        }

        String pins = event.headers().get(Addresses.PARAM_PINS);
        switch (GameManagerAction.fromHttpMethod(event.headers().get(Addresses.EVENT_BUS_MSG_HEADER_ACTION))) {
            case CREATE_GAME:
                gameStore.newGame();
                event.reply(EMPTY_MESSAGE);
                break;

            case GET_GAME:
                event.reply(gameStore.getGame(id).toJsonString());
                break;

            case DELETE_GAME:
                gameStore.deleteGame(id);
                event.reply(EMPTY_MESSAGE);
                break;

            case UPDATE_GAME:
                final Game game = gameStore.getGame(id);
                game.pinsKnocked(Integer.parseInt(pins));
                gameStore.updateGame(game);
                event.reply(EMPTY_MESSAGE);
                break;

            default:
                event.fail(FAILURE_CODE_INVALID_INPUT, MSG_INVALID_ACTION);
        }
    }

    private void chain(Future<Void> startFuture, AsyncResult<Void> newGameConsumer) {
        if (newGameConsumer.succeeded()) {
            LOG.info(getClass().getSimpleName() + " was deployed successfully.");
            startFuture.complete();
        } else {
            LOG.error(getClass().getSimpleName() + " wasn't deployed successfully.", newGameConsumer.cause());
            startFuture.fail(newGameConsumer.cause());
        }
    }

}
