package io.deeplay.model.piece;

import java.util.List;

public class Rook extends Piece {
    public Rook(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public List<int[]> getPossibleMoves(Piece[][] board) {
        return null;
    }

}
