package io.deeplay.model.move;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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

    public Move getLastMove() throws NoSuchElementException {
        if (moveHistory.size() > 0) {
            return moveHistory.get(moveHistory.size() - 1);
        }

        throw new NoSuchElementException("Ходов еще не было");
    }

    public void clearHistory() {
        moveHistory.clear();
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }
}