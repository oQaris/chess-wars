package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.List;

public abstract class Piece {
    Coordinates coordinates;
    public Color color;

    public Piece(Coordinates coordinates, Color color) {
        this.coordinates = coordinates;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract List<Integer> getPossibleMoves(Board board);

    public abstract boolean canMoveAt(Coordinates coordinates, Board board);  // rename at isValidMove
}
