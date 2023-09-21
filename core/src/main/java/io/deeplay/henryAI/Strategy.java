package io.deeplay.henryAI;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;

public interface Strategy {
    int evaluate(Board board, Color color);
}

