package io.deeplay.piece;

import java.util.List;

public class King extends Piece {
    public King(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public List<int[]> getPossibleMoves(Piece[][] board) {
        return null;
    }
}
