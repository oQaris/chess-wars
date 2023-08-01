package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
import io.deeplay.service.MoveService;
import io.deeplay.service.PieceService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class GameSession {

    private final Player player1;
    private final Player player2;
    private final GameType gameType;

    public GameSession(Player player1, Player player2, GameType gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
    }

    public void startGameSession() {
        GameInfo gameInfo = new GameInfo(player1, player2);

        while(true) {
            Color currentColor = gameInfo.getCurrentMoveColor();
            System.out.println(currentColor);
            Player playerWhoMoves = choosePlayer(currentColor);
            System.out.println("current player: " + playerWhoMoves.getClass().getSimpleName());
            List<Piece> possiblePiecesToMove = PieceService.getPiecesPossibleToMove(gameInfo.getCurrentBoard(), currentColor);
            System.out.println("Number of Pieces you can move: " + possiblePiecesToMove.size());
            Move move = playerWhoMoves.getMove(possiblePiecesToMove, gameInfo);

            gameInfo.move(move);

            boolean isFinished = GameState.check(gameInfo.getCurrentBoard());
            if (isFinished == true) endGame();
        }
    }

    // метод выбирает, кто ходит в данный момент
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
