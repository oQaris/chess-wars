package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

import static io.deeplay.model.Board.printBoardOnce;

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
        while(true) {
            Color currentColor = gameInfo.getCurrentMoveColor();
            System.out.println(currentColor);

            Player playerWhoMoves = choosePlayer(currentColor);
            System.out.println("current player: " + playerWhoMoves.getClass().getSimpleName());

            Move move = playerWhoMoves.getMove(gameInfo.getCurrentBoard(), currentColor);

            gameInfo.move(move);
            printBoardOnce(gameInfo.getCurrentBoard());

            boolean isFinished = GameState.check(gameInfo.getCurrentBoard());
            if (isFinished == true) endGame();
        }
    }

    /**
     * Метод возвращает игрока, который должен ходить в текущем ходе
     *
     * @param movingColor текущий цвет хода
     * @return игрока, который ходит
     */
    public Player choosePlayer(Color movingColor) {
        if (player1.getColor() == movingColor) return player1;
        else return player2;
    }

    public void endGame() {
        System.exit(0);
    }

    public GameType getGameType() {
        return gameType;
    }
}
