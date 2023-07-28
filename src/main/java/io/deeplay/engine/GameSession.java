package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.model.Board;
import io.deeplay.model.Piece;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
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
        Board board = new Board();
        GameInfo gameInfo = new GameInfo(board, player1, player2);

        while(true) {
            Player playerWhoMoves = choosePlayer(gameInfo.getCurrentMoveColor());
            List<Piece> possiblePiecesToMove = PieceService.getPossibleToMovePieces();
            System.out.println("Choose piece to make a move: ");
            Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());
            scanner.nextLine();
            // ...
            // Пользователь выбирает фигуру, которой будет ходить
            // ...

            Piece selectedPiece = possiblePiecesToMove.get(0);

            return;
        }
    }

    // метод выбирает, кто ходит в данный момент
    public Player choosePlayer(Color movingColor) {
        if (player1.getColor() == movingColor) return player1;
        else return player2;
    }

    public GameType getGameType() {
        return gameType;
    }
}
