package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.model.player.Player;

public class GameInfo {

    private Player player1;
    private Player player2;
    private Color currentMoveColor;
    private Board currentBoard;
    private MoveHistory moveHistory;

    public GameInfo(Board currentBoard, Player player1, Player player2) {
        this.currentMoveColor = Color.WHITE;
        this.currentBoard = currentBoard;
        this.moveHistory = new MoveHistory();
        this.player1 = player1;
        this.player2 = player2;
    }

    public GameInfo(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentMoveColor = Color.WHITE;
        this.currentBoard = new Board();
        this.moveHistory = new MoveHistory();
    }

    public void move(Move move) {
        currentBoard.move(move);
        moveHistory.addMove(move);
        if (currentMoveColor == Color.WHITE) currentMoveColor = Color.BLACK;
        else currentMoveColor = Color.WHITE;
    }

    public Color getCurrentMoveColor() {
        return currentMoveColor;
    }

    public void setCurrentMoveColor(Color currentMoveColor) {
        this.currentMoveColor = currentMoveColor;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }


}
