package io.deeplay.model.piece;

import io.deeplay.model.Board;

import java.util.ArrayList;
import java.util.List;

public class Empty extends Piece {
    public Empty(int x, int y, Color color) {
        super(x, y, Color.EMPTY);
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        return new ArrayList<>();
    }

    @Override
    public boolean canMoveAt(int x, int y, Board board) {
        return false;
    }
}
