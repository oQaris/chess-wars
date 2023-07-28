package io.deeplay.model.piece;

import java.util.List;

public class Queen extends Piece {
    public Queen(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public List<int[]> getPossibleMoves(Piece[][] board) {
        return null;
    }
}
