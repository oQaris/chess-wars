package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.domain.GameType;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

import static io.deeplay.model.Board.printBoardOnce;
@Slf4j
public class GameSession {
    private final Player player1;
    private final Player player2;
    private final GameType gameType;

    public GameSession(Player player1, Player player2, GameType gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
    }

    /**
     * Метод для создания текущей сессии игры. Отвечает за получение текущего хода;
     * выбора игрока, который должен ходить, исходя из текущего цвета игры; получение выбранного хода от игрока
     * и передача его в GameInfo. Также отвечает за завершение игры.
     */
    public void startGameSession() {
        GameInfo gameInfo = new GameInfo();
        printBoardOnce(gameInfo.getCurrentBoard());

        while (true) {
            Color currentColor = gameInfo.getCurrentMoveColor();
            Color enemyColor = gameInfo.getCurrentMoveColor().opposite();
            log.info("Ход {}", currentColor);
            System.out.println(currentColor);

            GameStates stateBeforeMove = GameStates.DEFAULT;

            if (GameState.isCheck(gameInfo.getCurrentBoard(), currentColor)) {
                stateBeforeMove = GameStates.CHECK;
            }

            Player playerWhoMoves = choosePlayer(currentColor);
            log.info("Текущий игрок: {}", playerWhoMoves.getClass().getSimpleName());
            System.out.println("current player: " + playerWhoMoves.getClass().getSimpleName());

            Move move = playerWhoMoves.getMove(gameInfo.getCurrentBoard(), currentColor);

            gameInfo.move(move);
            printBoardOnce(gameInfo.getCurrentBoard());

            if (GameState.isCheck(gameInfo.getCurrentBoard(), currentColor)) {
                log.info("Game ended {} win", currentColor.opposite());
                endGame("Game ended, because "
                        + currentColor + " is in check and can't move");
                return;
            }

            if (GameState.isMate(gameInfo.getCurrentBoard(), enemyColor)) {
                endGame("MATE, " + currentColor + " won");
                return;
            }

            if (GameState.isStaleMate(gameInfo.getCurrentBoard(), enemyColor)) {
                endGame("STALEMATE");
                return;
            }

            if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(gameInfo.getCurrentBoard())) {
                endGame("DRAW!");
                return;
            }
        }
    }

    /**
     * Метод возвращает игрока, который должен ходить в текущем ходе
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

    public void endGame(String textMessage) {
        log.info("Игра окончена {}", textMessage);
        System.out.println("Game ended due to: " + textMessage);
    }

    public GameType getGameType() {
        return gameType;
    }
}
