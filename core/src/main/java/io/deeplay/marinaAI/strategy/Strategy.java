package io.deeplay.marinaAI.strategy;

import io.deeplay.model.Board;

public interface Strategy {
    /**
     Метод evaluate вычисляет оценку позиции на шахматной доске.
     @param board         шахматная доска, для которой нужно вычислить оценку
     @return оценка позиции на доске
     */
    int evaluate(Board board);
}
