package io.deeplay.model.piece;

import java.util.List;

public class Knight extends Piece {
    public Knight(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public List<int[]> getPossibleMoves(Piece[][] board) {
        return null;
    }
}
