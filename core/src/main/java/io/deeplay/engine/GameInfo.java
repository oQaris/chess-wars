package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveHistory;

public class GameInfo {
    private Color currentMoveColor;
    private final Board currentBoard;
    private final MoveHistory moveHistory;

    public GameInfo() {
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
