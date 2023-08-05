package io.deeplay.service;

import io.deeplay.model.move.MoveHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveHistoryServiceTest {

    @Test
    void is50Moves() {
        final MoveHistory moveHistory = new MoveHistory();

        moveHistory.setMovesWithoutTake(49);
        assertFalse(MoveHistoryService.is50Moves(moveHistory));

        moveHistory.setMovesWithoutTake(50);
        assertTrue(MoveHistoryService.is50Moves(moveHistory));
    }
}