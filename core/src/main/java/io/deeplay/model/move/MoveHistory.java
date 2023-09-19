package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MoveHistory {
    private final List<Move> moveHistory;
    private int movesWithoutTake;

    public MoveHistory() {
        moveHistory = new ArrayList<>();
    }

    /**
     * Метод добавляет переданный Move в MoveHistory. Если Move не изменяет материальное состояние на доске -
     * инкрементирует movesWithoutTake
     * @param move переданный ход
     */
    public void addMove(Move move) {
        moveHistory.add(move);

        if (move.moveType() != MoveType.TAKE) {
            movesWithoutTake++;
        } else {
            movesWithoutTake = 0;
        }
    }

    /**
     * Метод удаляет последний Move из MoveHistory
     */
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

    /**
     * Метод для получения последнего хода из истории
     * @return последний Move из MoveHistory
     */
    public Move getLastMove() {
        if (!moveHistory.isEmpty()) {
            return moveHistory.get(moveHistory.size() - 1);
        } else return new Move(null, null, null, null);
    }

    /**
     * Метод очищает историю ходов
     */
    public void clearHistory() {
        moveHistory.clear();
    }
}