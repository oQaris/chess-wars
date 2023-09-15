package io.deeplay.marinaAI.utils;

import io.deeplay.model.move.Move;

public class ScoredMove {
    private final int score;
    private final Move move;

    public ScoredMove(int score, Move move) {
        this.score = score;
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }
}
