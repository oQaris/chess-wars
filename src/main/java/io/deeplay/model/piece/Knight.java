package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.List;

public class Knight extends Piece {
    public Knight(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        return null;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        return false;
    }
}
