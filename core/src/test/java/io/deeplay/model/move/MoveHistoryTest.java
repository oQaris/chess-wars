package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveHistoryTest {

    private Move move1;
    private Move move2;

    @BeforeEach
    void initialize() {
        Coordinates coordinates1 = new Coordinates(1, 2);
        Coordinates coordinates2 = new Coordinates(2, 4);
        move1 = new Move(coordinates1, coordinates2, MoveType.ORDINARY);
        move2 = new Move(coordinates2, coordinates1, MoveType.ORDINARY);
    }

    @Test
    void addMove() {
        final MoveHistory moveHistory = new MoveHistory();
        int initialSize = moveHistory.getMoveHistory().size();

        moveHistory.addMove(move1);
        int currentSize = moveHistory.getMoveHistory().size();
        int expectedSize = 1;

        Assertions.assertEquals(expectedSize, currentSize);
        Assertions.assertNotEquals(initialSize, currentSize);
    }

    @Test
    void removeLastMove() {
        final MoveHistory moveHistory = new MoveHistory();

        moveHistory.addMove(move1);
        moveHistory.addMove(move2);
        moveHistory.removeLastMove();

        Move lastMove = moveHistory.getMoveHistory().get(moveHistory.getMoveHistory().size() - 1);

        Assertions.assertEquals(move1, lastMove);
    }

    @Test
    void clearHistory() {
        final MoveHistory moveHistory = new MoveHistory();

        moveHistory.addMove(move1);
        moveHistory.addMove(move2);
        moveHistory.clearHistory();

        int expectedSize = 0;
        int actualSize = moveHistory.getMoveHistory().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }
}