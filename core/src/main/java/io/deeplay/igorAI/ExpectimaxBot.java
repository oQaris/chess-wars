package io.deeplay.igorAI;

import io.deeplay.igorAI.ai_agent.ExpectimaxAgent;
import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.service.IUserCommunication;

public class ExpectimaxBot extends Bot {
    /** Глубина просчета функции для Экспектимакса **/
    private static final int DEPTH = 3;
    public ExpectimaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    /**
     * Переопределенный метод, который вызывает метод из ExpectimaxAgent для получения лучшего хода для текущей доски
     * @param board текущее состояние доски
     * @param currentColor цвет текущего хода
     * @return лучший Move
     */
    @Override
    public Move getMove(Board board, Color currentColor) {
        return new ExpectimaxAgent().getBestMove(board, DEPTH, getColor());
    }
}
