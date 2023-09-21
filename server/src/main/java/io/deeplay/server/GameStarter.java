package io.deeplay.server;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameSession;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static io.deeplay.communication.converter.Converter.convertGameTypeDTO;

public class GameStarter implements Runnable {
    private static final Logger logger = LogManager.getLogger(GameStarter.class);
    private final List<ClientHandler> clients;
    private final GameType gameType;
    private final Player serverPlayer1;
    private final Player serverPlayer2;

    public GameStarter(List<ClientHandler> clients, GameType gameType) {
        this.clients = clients;
        this.gameType = gameType;
        serverPlayer1 = clients.get(0).getPlayer();
        serverPlayer2 = clients.get(1).getPlayer();
    }

    @Override
    public void run() {
        startGame();
    }

    /**
     * Метод создает новую сессию игры из переданных в конструкторе класса игроках и типа игры. Переопределяет
     * методы sendMove() для отправки хода другому клиенту, getMove() для получения хода от бота или человека,
     * sendGameEnd() для отпавки окончания игры другому клиенту
     */
    public void startGame() {
        System.out.println("GAME STARTED!");
        GameSession gameSession = new GameSession(serverPlayer1, serverPlayer2, convertGameTypeDTO(gameType)) {
            public void sendMove(Move move) {
                Color moveColor = getGameInfo().getCurrentMoveColor();
                if (moveColor.equals(serverPlayer1.getColor())) {
                    if (clients.get(0).getColor().equals(moveColor)) {
                        clients.get(1).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                        System.out.println("We sent move to other client: " + clients.get(1).getColor());
                    }

                    if (clients.get(1).getColor().equals(moveColor)) {
                        clients.get(0).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                        System.out.println("We sent move to other client: " + clients.get(0).getColor());
                    } else logger.error("Не удалось найти ход игрока");
                } else if (getGameInfo().getCurrentMoveColor().equals(serverPlayer2.getColor())) {
                    if (clients.get(0).getColor().equals(moveColor)) {
                        clients.get(1).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                    }

                    if (clients.get(1).getColor().equals(moveColor)) {
                        clients.get(0).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                    } else logger.error("Не удалось найти ход игрока");
                }
            }

            @Override
            public void sendGameEnd(List<String> gameEnd) {
                Color moveColor = getGameInfo().getCurrentMoveColor();

                if (clients.get(0).getColor().equals(moveColor)) {
                    EndGameDTO endGameDTO1 = clients.get(0).getEndGame(gameEnd);
                    String jsonEndGameDTO1 = SerializationService.convertEndGameDTOtoJSON(endGameDTO1);

                    clients.get(0).sendEndGameToClient(jsonEndGameDTO1);
                    clients.get(1).sendEndGameToClient(jsonEndGameDTO1);
                } else if (clients.get(1).getColor().equals(moveColor)) {
                    EndGameDTO endGameDTO2 = clients.get(1).getEndGame(gameEnd);
                    String jsonEndGameDTO2 = SerializationService.convertEndGameDTOtoJSON(endGameDTO2);

                    clients.get(0).sendEndGameToClient(jsonEndGameDTO2);
                    clients.get(1).sendEndGameToClient(jsonEndGameDTO2);
                } else {
                    logger.error("Ошибка отправки конца игры клиенту");
                }
            }

            @Override
            public void sendGameError(Exception exception, String error) {
                Color moveColor = getGameInfo().getCurrentMoveColor();

                if (clients.get(0).getColor().equals(moveColor)) {
                    ErrorResponseDTO errorResponseDTO1 = clients.get(0).getError(exception, error);
                    String jsonErrorDTO1 = SerializationService.convertErrorResponseDTOtoJSON(errorResponseDTO1);

                    clients.get(0).sendErrorToClient(jsonErrorDTO1);
                    clients.get(1).sendErrorToClient(jsonErrorDTO1);
                } else if (clients.get(1).getColor().equals(moveColor)) {
                    ErrorResponseDTO errorResponseDTO2 = clients.get(1).getError(exception, error);
                    String jsonErrorDTO2 = SerializationService.convertErrorResponseDTOtoJSON(errorResponseDTO2);

                    clients.get(0).sendErrorToClient(jsonErrorDTO2);
                    clients.get(1).sendErrorToClient(jsonErrorDTO2);
                } else {
                    logger.error("Ошибка отправки конца игры клиенту");
                }
            }

            private Object eventResult;

            /**
             Метод getEvent(Color color) возвращает результат события в зависимости от переданного цвета.
             @param color цвет, для которого нужно получить результат события
             @return объект типа Object, содержащий результат события
             @throws IllegalArgumentException если передан недопустимый цвет */
            public Object getEvent(Color color) {
                if (clients.get(0).getColor().equals(color)) {
                    eventResult = clients.get(0).listenJson();
                } else if (clients.get(1).getColor().equals(color)) {
                    eventResult = clients.get(1).listenJson();
                } else {
                    throw new IllegalArgumentException();
                }

                return eventResult;
            }

            @Override
            public Move getMove(Player player, Color color) {
                Object object = getEvent(color);

                if (object instanceof Move move) {
                    return move;
                }

                throw new IllegalStateException();
            }

            @Override
            public List<String> getEndGame(Color color) {
                Object object = eventResult;

                if (object instanceof List<?> endGame) {
                    if (endGame.get(0) instanceof String) {
                        return (List<String>) endGame;
                    }
                }

                throw new IllegalArgumentException();
            }

            @Override
            public List<Object> getError() {
                Object object = eventResult;

                if (object instanceof List<?> error) {
                    if (error.get(0) instanceof Exception) {
                        return (List<Object>) error;
                    }
                }

                throw new IllegalArgumentException();
            }
        };

        gameSession.startGameSession();
    }
}
