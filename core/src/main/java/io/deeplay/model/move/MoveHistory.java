package io.deeplay.model.move;

import io.deeplay.domain.MoveType;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory {
    private final List<Move> moveHistory;
    private int movesWithoutTake;

    public MoveHistory() {
        moveHistory = new ArrayList<>();
    }

    public void addMove(Move move) {
        moveHistory.add(move);

        if (move.moveType() != MoveType.TAKE) {
            movesWithoutTake++;
        } else {
            movesWithoutTake = 0;
        }
    }

    public void removeLastMove() {
        if (!moveHistory.isEmpty()) {
            moveHistory.remove(moveHistory.size() - 1);
        }
    }

    /**
     * Метод создан исключительно для использования при создании дубликата Board.
     * Для добавления Move использовать метод addMove()
     */
    public void setLastMove(Move move) {
        moveHistory.add(move);
    }

    public Move getLastMove() {
        if (!moveHistory.isEmpty()) {
            return moveHistory.get(moveHistory.size() - 1);
        } else return new Move(null, null, null, null);
    }

    public void clearHistory() {
        moveHistory.clear();
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public void setMovesWithoutTake(int movesWithoutTake) {
        this.movesWithoutTake = movesWithoutTake;
    }

    public int getMovesWithoutTake() {
        return movesWithoutTake;
    }
}