package io.deeplay.model.piece;

import io.deeplay.model.Board;

import java.util.List;

public abstract class Piece {
    public int x;
    public int y;
    public Color color;

    public Piece(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract List<Integer> getPossibleMoves(Board board);

     public abstract boolean canMoveAt(int x, int y, Board board);
}
