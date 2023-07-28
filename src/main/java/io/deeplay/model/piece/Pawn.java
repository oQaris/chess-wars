package io.deeplay.model.piece;

import java.util.List;

public class Pawn extends Piece{
    public Pawn(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public List<int[]> getPossibleMoves(Piece[][] board) {
        return null;
    }
}
