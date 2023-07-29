package io.deeplay.model.piece;

import io.deeplay.model.Board;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        // метод
        return null;
    }

    @Override
    public boolean canMoveAt(int x, int y, Board board) {
        return false;
    }
}
