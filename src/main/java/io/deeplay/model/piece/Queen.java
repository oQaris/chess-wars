package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        return new ArrayList<>();
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        return false;
    }
}
