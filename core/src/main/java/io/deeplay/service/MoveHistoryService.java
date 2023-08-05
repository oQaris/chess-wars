package io.deeplay.service;

import io.deeplay.model.move.MoveHistory;

public class MoveHistoryService {
    public static boolean is50Moves(MoveHistory moveHistory) {
        return moveHistory.getMovesWithoutTake() >= 50;
    }
}
