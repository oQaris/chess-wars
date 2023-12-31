package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.domain.GameType;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.deeplay.model.Board.printBoardOnce;

@Slf4j
public class GameSession {
    private final Player player1;
    private final Player player2;
    private final List<String> gameEnd = new ArrayList<>();
    private final List<Object> gameError = new ArrayList<>();
    @Getter
    private final GameType gameType;
    @Getter
    private final GameInfo gameInfo;

    public GameSession(Player player1, Player player2, GameType gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
        this.gameInfo = new GameInfo();
    }

    /**
     * Метод для создания текущей сессии игры. Отвечает за получение текущего хода;
     * выбора игрока, который должен ходить, исходя из текущего цвета игры; получение выбранного хода от игрока
     * и передача его в GameInfo. Если конец игры - вызывает функцию endGame().
     */
    public void startGameSession() {
        printBoardOnce(gameInfo.getCurrentBoard());
        while (true) {
            Color currentColor = gameInfo.getCurrentMoveColor();
            Color enemyColor = gameInfo.getCurrentMoveColor().opposite();
            log.info("Ход {}", currentColor);
            System.out.println(currentColor);

            Player playerWhoMoves = choosePlayer(currentColor);
            log.info("Текущий игрок: {}", playerWhoMoves.getClass().getSimpleName());
            System.out.println("current player: " + playerWhoMoves.getClass().getSimpleName());

            getEvent(playerWhoMoves, currentColor);

            if (GameState.isMate(gameInfo.getCurrentBoard(), enemyColor)) {
                gameEnd.add(0, GameStates.CHECKMATE.toString());
                gameEnd.add(1, currentColor.toString());

                endGame("CHECKMATE, " + currentColor + " won");
                return;
            }

            if (GameState.isStaleMate(gameInfo.getCurrentBoard(), enemyColor)) {
                gameEnd.add(0, GameStates.STALEMATE.toString());
                gameEnd.add(1, currentColor.toString());

                endGame("STALEMATE");
                return;
            }

            if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(gameInfo.getCurrentBoard())) {
                gameEnd.add(0, GameStates.DRAW.toString());
                gameEnd.add(1, Color.EMPTY.toString());

                endGame("DRAW!");
                return;
            }
        }
    }

    public void getEvent(Player playerWhoMoves, Color currentColor) {
        Move move;
        List<String> gameEndList;
        List<Object> gameErrorList;

        try {
            move = getMove(playerWhoMoves, currentColor);

            sendMove(move);
            gameInfo.move(move);
        } catch (IllegalStateException e1) {
            try {
                gameEndList = getEndGame(currentColor);

                gameEnd.add(0, gameEndList.get(0));
                gameEnd.add(1, gameEndList.get(1));

                endGame("SURRENDER, " + gameEndList.get(1) + "won");
            } catch (IllegalArgumentException e2) {
                try {
                    gameErrorList = getError();

                    gameError.add(0, gameErrorList.get(0));
                    gameError.add(1, gameErrorList.get(1));

                    catchGameError((Exception) gameError.get(0), (String) gameError.get(1));
                } catch (IllegalStateException e3) {
                    log.error("ошибка в получении информации");
                }
            }
        }
    }

    /**
     * Метод возвращает игрока, который должен ходить на текущий момент
     *
     * @param movingColor текущий цвет хода
     * @return игрока, который ходит
     */
    public Player choosePlayer(Color movingColor) {
        if (player1.getColor() == movingColor) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Метод обрабатывает конец игры.
     *
     * @param textMessage подробное сообщение о том, как завершилась игра
     */
    public void endGame(String textMessage) {
        sendGameEnd(gameEnd);
        log.info("Игра окончена {}", textMessage);
        printBoardOnce(gameInfo.getCurrentBoard());
    }

    /**
     * Метод обрабатывает конец игры из-за ошибки.
     *
     * @param message   подробное сообщение о том, какая ошибка произошла
     * @param exception ошибка, по причине которой завершилась игра
     */
    public void catchGameError(Exception exception, String message) {
        sendGameError(exception, message);
        log.info("Игра закончена ошибкой {}", message);
    }

    /**
     * Получает Move из класса player (от Bot или Human)
     *
     * @param player текущий игрок
     * @param color  текущий цвет
     * @return полученный Move
     */
    public Move getMove(Player player, Color color) {
        return player.getMove(gameInfo.getCurrentBoard(), color);
    }

    /**
     * Получает List<Object>
     *
     * @return полученный List<Object>
     */
    public List<Object> getError() {
        return new ArrayList<>();
    }

    /**
     * Метод отправляет событие окончания игры.
     *
     * @param gameEnd список строк, содержащий информацию об окончании игры
     */
    public void sendGameEnd(List<String> gameEnd) {
    }

    /**
     * Метод отправляет информацию о ходе игры.
     *
     * @param move объект типа Move, содержащий информацию о ходе
     */
    public void sendMove(Move move) {
    }

    /**
     * Метод возвращает список строк, содержащий информацию об окончании игры для указанного цвета.
     *
     * @param color цвет, для которого нужно получить информацию об окончании игры
     * @return список строк, содержащий информацию об окончании игры
     */
    public List<String> getEndGame(Color color) {
        return gameEnd;
    }

    /**
     * Метод возвращает строковое представление информации об окончании игры.
     *
     * @return строковое представление информации об окончании игры
     */
    public String getGameEnd() {
        return gameEnd.toString();
    }

    /**
     * Метод возвращает список объектов, содержащий информацию об ошибках короля.
     *
     * @return список объектов, содержащий информацию об ошибках короля
     */
    public List<Object> getKingError() {
        return GameState.getErrorList();
    }

    /**
     * Метод отправляет информацию об ошибке игры.
     *
     * @param exception исключение, возникшее в игре
     * @param error     описание ошибки
     */
    public void sendGameError(Exception exception, String error) {
    }
}
