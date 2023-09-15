package io.deeplay.marinaAI.strategy;

import io.deeplay.model.Board;

public interface Strategy {
    int evaluate(Board board);
}
