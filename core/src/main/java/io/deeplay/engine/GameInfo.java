package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import lombok.Getter;

@Getter
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
     * Метод делает движение, путем обновления текущего состояния доски. Вызывает метод изменения текущего хода.
     * @param move ход игрока
     */
    public void move(Move move) {
        currentBoard.move(move);
        changeCurrentMoveColor();
    }

    /**
     * Метод меняет цвет текущего хода
     */
    protected void changeCurrentMoveColor() {
        if (currentMoveColor == Color.WHITE) currentMoveColor = Color.BLACK;
        else currentMoveColor = Color.WHITE;
    }
}