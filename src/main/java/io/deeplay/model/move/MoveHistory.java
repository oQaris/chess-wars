package io.deeplay.model.move;


import io.deeplay.model.move.Move;

import java.util.ArrayList;

public class MoveHistory {
    private ArrayList<Move> moveHistory;

    public MoveHistory() {
        moveHistory = new ArrayList<>();
    }

    public void addMove(Move move) {
        moveHistory.add(move);
    }

    public void removeLastMove() {
        if (!moveHistory.isEmpty()) {
            moveHistory.remove(moveHistory.size() - 1);
        }
    }

    public void clearHistory() {
        moveHistory.clear();
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }
}