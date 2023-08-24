package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;

public class GameInfo {
    private Color currentMoveColor;
    private final Board currentBoard;

    public GameInfo() {
        this.currentMoveColor = Color.WHITE;
        this.currentBoard = new Board();
    }

    public GameInfo(Color currentMoveColor) {
        this.currentMoveColor = currentMoveColor;
        this.currentBoard = new Board();
    }

    /**
     * Метод делает движение, путем обновления текущего состояния доски, добавления хода в MoveHistory. Также
     * меняет цвет текущего хода
     *
     * @param move ход игрока
     */
    public void move(Move move) {
        currentBoard.move(move);
//        if (currentMoveColor == Color.WHITE) currentMoveColor = Color.BLACK;
//        else currentMoveColor = Color.WHITE;
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
}