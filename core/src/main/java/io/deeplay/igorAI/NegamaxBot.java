package io.deeplay.igorAI;

import io.deeplay.domain.Color;
import io.deeplay.igorAI.ai_agent.NegamaxAgent;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.service.IUserCommunication;

public class NegamaxBot extends Bot {
    /** Глубина просчета функции для Негамакса **/
    private static final int DEPTH = 3;
    public NegamaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    /**
     * Переопределенный метод, который вызывает метод из NegamaxAgent для получения лучшего хода для текущей доски
     * @param board текущее состояние доски
     * @param currentColor цвет текущего хода
     * @return лучший Move
     */
    @Override
    public Move getMove(Board board, Color currentColor) {
        return new NegamaxAgent().getBestMove(board, DEPTH, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, getColor());
    }
}
