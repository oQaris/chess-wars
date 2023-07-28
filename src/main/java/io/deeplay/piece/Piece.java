package io.deeplay.piece;

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

    public abstract List<int[]> getPossibleMoves(Piece[][] board);
}
