package com.freeletics.bowlinggg.verticle.game;

import com.freeletics.bowlinggg.config.Addresses;
import com.freeletics.bowlinggg.verticle.game.model.Game;
import com.freeletics.bowlinggg.verticle.game.store.GameStore;
import com.freeletics.bowlinggg.verticle.game.store.InMemGameStore;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
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
    private static final int FAILURE_CODE_NOT_FOUND = 404;
    private static final int FAILURE_CODE_INVALID_INPUT = 400;

    private GameStore gameStore;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        gameStore = new InMemGameStore();
    }

    @Override
    public void start(Future<Void> startFuture) {
        final EventBus eventBus = vertx.eventBus();

        eventBus.localConsumer(Addresses.GAMES_ENDPOINT).handler(this::replyToMsg)
                .completionHandler(newGameConsumer -> chain(startFuture, newGameConsumer));
    }

    private void replyToMsg(Message<Object> event) {
        String id = event.headers().get(Addresses.PARAM_ID);
        String pins = event.headers().get(Addresses.PARAM_PINS);
        switch (GameManagerAction.fromHttpMethod(event.headers().get("action"))) {
            case CREATE_GAME:
                gameStore.newGame();
                event.reply("");
                break;

            case GET_GAME:
                final Game gameById = gameStore.getGame(id);
                if (gameById == null) {
                    event.fail(FAILURE_CODE_NOT_FOUND, "Game not found!");
                } else {
                    event.reply(gameById.toJsonString());
                }
                break;

            case DELETE_GAME:
                final Game gameToDelete = gameStore.deleteGame(id);
                if (gameToDelete == null) {
                    event.fail(FAILURE_CODE_NOT_FOUND, "Game not found!");
                } else {
                    event.reply("");
                }

                break;

            case UPDATE_GAME:
                final Game gameToUpdate = gameStore.getGame(id);
                if (gameToUpdate == null) {
                    event.fail(FAILURE_CODE_NOT_FOUND, "Game not found!");
                    return;
                }

                event.reply(gameToUpdate.getScore());
                int pinsNumber;
                try {
                    pinsNumber = Integer.parseInt(pins);
                } catch (NumberFormatException e) {
                    event.fail(FAILURE_CODE_INVALID_INPUT, "Invalid pins value!");
                    return;
                }

                gameToUpdate.pinsKnocked(pinsNumber);
                gameStore.updateGame(gameToUpdate);
                event.reply("");
                break;

            default:
                event.fail(FAILURE_CODE_INVALID_INPUT, "Invalid action!");
        }
    }

    private void chain(Future<Void> startFuture, AsyncResult<Void> newGameConsumer) {
        if (newGameConsumer.succeeded()) {
            LOG.info(getClass().getSimpleName() + " was deployed successfully.");
            startFuture.complete();
        } else {
            startFuture.fail(newGameConsumer.cause());
        }
    }

}
